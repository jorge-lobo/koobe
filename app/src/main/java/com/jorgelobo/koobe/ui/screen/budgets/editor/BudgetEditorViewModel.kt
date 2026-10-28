package com.jorgelobo.koobe.ui.screen.budgets.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.core.model.resolve
import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.settings.GetUserSettingsUseCase
import com.jorgelobo.koobe.domain.usecase.budget.DeleteBudgetUseCase
import com.jorgelobo.koobe.domain.usecase.budget.GetBudgetByIdUseCase
import com.jorgelobo.koobe.domain.usecase.budget.SaveBudgetUseCase
import com.jorgelobo.koobe.domain.usecase.category.GetCategoryByIdUseCase
import com.jorgelobo.koobe.domain.usecase.subcategory.GetSubcategoryByIdUseCase
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.budgets.editor.state.BudgetFormState
import com.jorgelobo.koobe.ui.screen.budgets.editor.state.BudgetUiStateInternal
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorTarget
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.handleConfirmationDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class BudgetEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getUserSettings: GetUserSettingsUseCase,
    private val getBudgetById: GetBudgetByIdUseCase,
    private val getCategoryById: GetCategoryByIdUseCase,
    private val getSubCategoryById: GetSubcategoryByIdUseCase,
    private val saveBudget: SaveBudgetUseCase,
    private val deleteBudget: DeleteBudgetUseCase
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

    private val userSettingsFlow: StateFlow<Pair<AppLanguage, CurrencyType>> =
        getUserSettings()
            .map { settings ->
                settings.language to settings.currency
            }
            .catch { exception ->
                if (exception is CancellationException) throw exception

                showSnackBar(R.string.snackBar_load_settings_error)

                emit(
                    AppLanguage.ENGLISH to CurrencyType.EUR
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = AppLanguage.ENGLISH to CurrencyType.EUR
            )

    private val editorDataFlow = flow {
        when (val editorConfig = config) {
            is BudgetEditorConfig.Create -> {
                val category = getCategoryById(editorConfig.categoryId)
                    ?: error("Category not found: ${editorConfig.categoryId}")
                val subcategory = getSubCategoryById(editorConfig.subcategoryId)
                    ?: error("Subcategory not found: ${editorConfig.subcategoryId}")

                emit(
                    BudgetEditorUiState.initial(
                        config = config,
                        budget = Budget.empty(),
                        category = category,
                        subcategory = subcategory,
                        isRepeat = false,
                        period = PeriodType.MONTHLY
                    )
                )
            }

            is BudgetEditorConfig.Edit -> {
                val budget = getBudgetById(editorConfig.budgetId)
                    ?: error("Budget not found: ${editorConfig.budgetId}")

                val category = getCategoryById(budget.categoryId)
                    ?: error("Category not found: ${budget.categoryId}")
                val subcategory = getSubCategoryById(budget.subcategoryId)
                    ?: error("Subcategory not found: ${budget.subcategoryId}")

                emit(
                    BudgetEditorUiState.initialFromBudget(
                        config = config,
                        budget = budget,
                        category = category,
                        subcategory = subcategory,
                        isRepeat = budget.repeat,
                        period = budget.period
                    )
                )
            }
        }
    }

    private val baseStateFlow: StateFlow<BudgetEditorUiState> =
        combine(
            editorDataFlow,
            userSettingsFlow
        ) { base, settings ->
            base.copy(
                language = settings.first,
                currencyType = settings.second
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = BudgetEditorUiState.initialEmpty().copy(
                config = config
            )
        )


    val uiState: StateFlow<BudgetEditorUiState> =
        combine(
            baseStateFlow,
            formState,
            uiInternalState
        ) { base, form, internal ->

            val updatedLimitInput = form.limitAmountInput.resolve(base.limitAmountInput)
            val updatedLimit = updatedLimitInput.toDoubleOrNull() ?: base.limitAmount

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
                isSaving = internal.isSaving,
                isDeleting = internal.isDeleting,
                isLoading = base.isLoading || internal.isSaving || internal.isDeleting
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BudgetEditorUiState.initialEmpty()
        )

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
            baseState = baseStateFlow.value
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
        val state = uiState.value
        val isEditMode = config is BudgetEditorConfig.Edit

        if (!state.isValid) {
            showSnackBar(R.string.snackBar_save_budget_error)
            return
        }

        val budget = state.budget.copy(
            categoryId = state.category.id,
            subcategoryId = state.subcategory.id,
            period = state.period,
            repeat = state.isRepeat,
            limitAmount = state.limitAmount
        )

        viewModelScope.launch {
            uiInternalState.update { it.copy(isSaving = true) }

            runCatching {
                saveBudget(
                    budget = budget,
                    isEditorMode = isEditMode
                )
            }.onSuccess {
                uiInternalState.update { it.copy(isSaving = false) }
                navigateBack()
            }.onFailure {
                uiInternalState.update { it.copy(isSaving = false) }
                showSnackBar(R.string.snackBar_save_budget_error)
            }
        }
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
        val mode = when (config) {
            is BudgetEditorConfig.Create -> CategorySelectorMode.CREATE_BUDGET
            is BudgetEditorConfig.Edit -> CategorySelectorMode.EDIT_BUDGET
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
        handleConfirmationDialog(
            current = uiInternalState.value.discardDialog,
            action = action,
            updateState = { newDialogState ->
                uiInternalState.update { currentState ->
                    currentState.copy(discardDialog = newDialogState)
                }
            },
            onConfirmed = { navigateBack() }
        )
    }

    private fun handleDeleteDialog(action: ConfirmationDialogAction) {
        handleConfirmationDialog(
            current = uiInternalState.value.deleteDialog,
            action = action,
            updateState = { newDialogState ->
                uiInternalState.update { currentState ->
                    currentState.copy(deleteDialog = newDialogState)
                }
            },
            onConfirmed = { deleteCurrentBudget() }
        )
    }

    private fun deleteCurrentBudget() {
        val budget = uiState.value.budget

        viewModelScope.launch {
            uiInternalState.update { it.copy(isDeleting = true) }

            runCatching {
                deleteBudget(budget)
            }.onSuccess {
                uiInternalState.update { it.copy(isDeleting = false) }
                navigateBack()
            }.onFailure {
                uiInternalState.update { it.copy(isDeleting = false) }
                showSnackBar(messageRes = R.string.snackBar_delete_budget_error)
            }
        }
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