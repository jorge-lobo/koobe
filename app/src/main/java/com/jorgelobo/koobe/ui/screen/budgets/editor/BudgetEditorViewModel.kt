package com.jorgelobo.koobe.ui.screen.budgets.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.core.model.resolve
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.settings.GetUserSettingsUseCase
import com.jorgelobo.koobe.domain.usecase.budget.GetBudgetByIdUseCase
import com.jorgelobo.koobe.domain.usecase.category.GetCategoryByIdUseCase
import com.jorgelobo.koobe.domain.usecase.subcategory.GetSubcategoryByIdUseCase
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.screen.budgets.editor.state.BudgetFormState
import com.jorgelobo.koobe.ui.screen.budgets.editor.state.BudgetUiStateInternal
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorTarget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class BudgetEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserSettings: GetUserSettingsUseCase,
    private val getBudgetById: GetBudgetByIdUseCase,
    private val getCategoryById: GetCategoryByIdUseCase,
    private val getSubCategoryById: GetSubcategoryByIdUseCase
) : ViewModel() {

    private val config: BudgetEditorConfig =
        savedStateHandle.get<String>("config")
            ?.let { URLDecoder.decode(it, "UTF-8") }
            ?.let { Json.decodeFromString<BudgetEditorConfig>(it) }
            ?: error("Missing BudgetEditorConfig")

    private val _events = MutableSharedFlow<BudgetEditorEvent>()
    val events = _events.asSharedFlow()

    private val formState = MutableStateFlow(BudgetFormState())
    private val uiInternalState = MutableStateFlow(BudgetUiStateInternal())

    private val _baseState = MutableStateFlow(BudgetEditorUiState.initialEmpty())
    private val baseState = _baseState.asStateFlow()

    val uiState: StateFlow<BudgetEditorUiState> =
        combine(
            _baseState,
            formState,
            uiInternalState
        ) { base, form, internal ->

            val updatedLimitInput = form.limitInput.resolve(base.limitAmountInput)
            val updatedLimit = updatedLimitInput.toDoubleOrNull() ?: 0.0

            base.copy(
                category = form.category.resolve(base.category),
                subcategory = form.subcategory.resolve(base.subcategory),
                period = form.period.resolve(base.period),
                isRepeat = form.isRepeat.resolve(base.isRepeat),
                limitAmountInput = updatedLimitInput,
                limitAmount = updatedLimit,
                inputState = InputState.DEFAULT,
                discardDialog = internal.discardDialog,
                deleteDialog = internal.deleteDialog,
                isLoading = internal.isSaving || internal.isDeleting
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BudgetEditorUiState.initialEmpty()
        )

    init {
        observeUserSettings()
        loadEditor()
    }

    private fun observeUserSettings() {
        viewModelScope.launch {
            getUserSettings()
                .catch { exception ->
                    if (exception is CancellationException) throw exception

                    showSnackBar(R.string.snackBar_load_settings_error)
                }
                .collect { settings ->
                    _baseState.update {
                        it.copy(
                            language = settings.language,
                            currencyType = settings.currency
                        )
                    }
                }
        }
    }

    private fun loadEditor() {
        viewModelScope.launch {
            runCatching {
                val budget = if (config.isEditMode) {
                    getBudgetById(requireNotNull(config.budgetId))
                        ?: error("Budget not found: ${config.budgetId}")
                } else {
                    null
                }

                val categoryId = budget?.categoryId
                    ?: requireNotNull(config.categoryId) { "Missing categoryId for budget creation" }
                val subcategoryId = budget?.subcategoryId
                    ?: requireNotNull(config.subcategoryId) { "Missing subcategoryId for budget creation" }

                val category =
                    getCategoryById(categoryId) ?: error("Category not found: $categoryId")
                val subcategory = getSubCategoryById(subcategoryId)
                    ?: error("Subcategory not found: $subcategoryId")

                Triple(budget, category, subcategory)
            }.onSuccess { (budget, category, subcategory) ->
                _baseState.update { current ->
                    BudgetEditorUiState.initial(
                        config = config,
                        category = category,
                        subcategory = subcategory,
                        language = current.language,
                        currencyType = current.currencyType,
                        isRepeat = budget?.repeat ?: false,
                        period = budget?.period ?: PeriodType.MONTHLY,
                        limitAmount = budget?.limitAmount ?: 0.0
                    ).copy(
                        spentAmount = budget?.spentAmount ?: 0.0,
                        projectedAmount = budget?.projectedAmount ?: 0.0,
                        dailyAverage = budget?.dailyAverage ?: 0.0,
                        isLoading = false
                    )
                }
            }.onFailure {
                _baseState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = it.errorMessage ?: "Unable to load budget"
                    )
                }
                showSnackBar(R.string.snackBar_load_budget_error)
            }
        }
    }

    fun onIntent(intent: BudgetEditorIntent) {
        when (intent) {
            is BudgetEditorIntent.State -> reduceState(intent)
            is BudgetEditorIntent.Action -> handleAction(intent)
        }
    }

    private fun reduceState(intent: BudgetEditorIntent.State) {
        val result = BudgetEditorReducer.reduce(
            intent = intent,
            currentForm = formState.value,
            currentInternal = uiInternalState.value,
            baseState = baseState.value
        )

        formState.value = result.form
        uiInternalState.value = result.internal
    }

    private fun handleAction(intent: BudgetEditorIntent.Action) {
        when (intent) {
            BudgetEditorIntent.Action.SaveClicked -> handleSave()
            BudgetEditorIntent.Action.CloseClicked -> handleClose()
            BudgetEditorIntent.Action.RequestDeleteBudget ->
                handleDeleteDialog(ConfirmationDialogAction.Open)

            BudgetEditorIntent.Action.ChangeCategoryClicked ->
                handleChangeCategory()

            is BudgetEditorIntent.Action.DiscardDialogUpdated ->
                handleDiscardDialog(intent.action)

            is BudgetEditorIntent.Action.DeleteDialogUpdated ->
                handleDeleteDialog(intent.action)
        }
    }

    private fun handleSave() {

    }

    private fun handleClose() {
        if (formState.value.hasChanges) {
            handleDiscardDialog(ConfirmationDialogAction.Open)
        } else {
            navigateBack()
        }
    }

    private fun handleChangeCategory() {
        val state = uiState.value
        val mode = if (config.isEditMode) {
            CategorySelectorMode.EDIT_BUDGET
        } else {
            CategorySelectorMode.CREATE_BUDGET
        }

        val route = Route.CategorySelector.create(
            CategorySelectorConfig(
                mode = mode,
                target = CategorySelectorTarget.BUDGET_EDITOR,
                initialTransactionType = TransactionType.EXPENSE,
                initialCategoryId = state.category.id,
                initialSubcategoryId = state.subcategory.id
            )
        )

        navigateTo(route)
    }

    private fun handleDiscardDialog(action: ConfirmationDialogAction) {

    }

    private fun handleDeleteDialog(action: ConfirmationDialogAction) {

    }

    private fun showSnackBar(messageRes: Int) {
        emitEvent(
            BudgetEditorEvent.ShowSnackBar(
                messageRes,
                null,
                IconPack.WARNING
            )
        )
    }

    private fun navigateBack() {
        emitEvent(BudgetEditorEvent.ExitToOrigin)
    }

    private fun navigateTo(route: String) {
        emitEvent(BudgetEditorEvent.NavigateTo(route))
    }

    private fun emitEvent(event: BudgetEditorEvent) {
        viewModelScope.launch { _events.emit(event) }
    }
}