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
import com.jorgelobo.koobe.domain.usecase.budget.GetBudgetsBySubcategoryIdUseCase
import com.jorgelobo.koobe.domain.usecase.budget.HasExistingBudgetForPeriodUseCase
import com.jorgelobo.koobe.domain.usecase.budget.SaveBudgetUseCase
import com.jorgelobo.koobe.domain.usecase.category.GetCategoryByIdUseCase
import com.jorgelobo.koobe.domain.usecase.subcategory.GetSubcategoryByIdUseCase
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and business logic of the Budget Editor screen.
 *
 * This class handles both the creation of new budgets and the editing of existing ones,
 * reacting to [BudgetEditorIntent]s to update the [BudgetEditorUiState]. It manages asynchronous
 * data loading, input validation, duplicate budget detection, and persistence operations via
 * specialized use cases.
 *
 * It utilizes [SavedStateHandle] to retrieve navigation arguments and leverages
 * [BudgetEditorReducer] to maintain a clean separation between state transitions
 * and side effects.
 *
 * @property getBudgetById Use case to load an existing budget for editing.
 * @property getCategoryById Use case to retrieve category details for display.
 * @property getSubCategoryById Use case to retrieve subcategory details for display.
 * @property saveBudget Use case to persist or update a budget.
 * @property deleteBudget Use case to remove an existing budget.
 * @property getBudgetsBySubcategoryId Use case to check for existing budgets in the same subcategory.
 * @property hasExistingBudgetForPeriod Use case to validate that no other budget exists for the same period/subcategory.
 */
@HiltViewModel
class BudgetEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getUserSettings: GetUserSettingsUseCase,
    private val getBudgetById: GetBudgetByIdUseCase,
    private val getCategoryById: GetCategoryByIdUseCase,
    private val getSubCategoryById: GetSubcategoryByIdUseCase,
    private val saveBudget: SaveBudgetUseCase,
    private val deleteBudget: DeleteBudgetUseCase,
    private val getBudgetsBySubcategoryId: GetBudgetsBySubcategoryIdUseCase,
    private val hasExistingBudgetForPeriod: HasExistingBudgetForPeriodUseCase
) : ViewModel() {

    private val config: BudgetEditorConfig =
        savedStateHandle.get<String>("config")
            ?.let { URLDecoder.decode(it, "UTF-8") }
            ?.let { Json.decodeFromString<BudgetEditorConfig>(it) }
            ?: error("Missing BudgetEditorConfig")

    /**
     * Emits one-off UI events such as navigation actions and snackbar messages.
     */
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private val existingBudgetsFlow: Flow<List<BudgetUiModel>> =
        combine(
            baseStateFlow,
            formState
        ) { base, form ->
            val category = form.category.resolve(base.category)
            val subcategory = form.subcategory.resolve(base.subcategory)

            category to subcategory
        }
            .distinctUntilChangedBy { it.second.id }
            .flatMapLatest { (category, subcategory) ->
                if (config is BudgetEditorConfig.Create) {
                    getBudgetsBySubcategoryId(subcategory.id)
                        .map { budgets ->
                            budgets.map { budget ->
                                BudgetUiModel(
                                    budget = budget,
                                    category = category,
                                    subcategory = subcategory
                                )
                            }
                        }
                } else {
                    flowOf(emptyList())
                }
            }

    /**
     * Exposes the current state of the Budget Editor screen.
     *
     * This [StateFlow] represents the unified view state, synthesized from the editor's
     * configuration, user settings, form inputs, internal UI logic, and existing budget data.
     * It is updated reactively as the user interacts with the editor or as background data loads.
     */
    val uiState: StateFlow<BudgetEditorUiState> =
        combine(
            baseStateFlow,
            formState,
            uiInternalState,
            existingBudgetsFlow
        ) { base, form, internal, existingBudgets ->

            val updatedLimitInput = form.limitAmountInput.resolve(base.limitAmountInput)
            val updatedLimit = updatedLimitInput.toDoubleOrNull() ?: base.limitAmount

            val subcategory = form.subcategory.resolve(base.subcategory)

            base.copy(
                category = form.category.resolve(base.category),
                subcategory = subcategory,
                period = form.period.resolve(base.period),
                isRepeat = form.isRepeat.resolve(base.isRepeat),
                limitAmountInput = updatedLimitInput,
                limitAmount = updatedLimit,
                inputState = InputState.DEFAULT,
                discardDialog = internal.discardDialog,
                deleteDialog = internal.deleteDialog,
                duplicateBudgetDialog = internal.duplicateBudgetDialog,
                isSaving = internal.isSaving,
                isDeleting = internal.isDeleting,
                isLoading = base.isLoading || internal.isSaving || internal.isDeleting,
                existingBudgets = existingBudgets,
                isExistingBudgetsDialogVisible =
                    config is BudgetEditorConfig.Create &&
                            existingBudgets.isNotEmpty() &&
                            internal.existingBudgetsDialogDismissedFor != subcategory.id
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BudgetEditorUiState.initialEmpty()
        )

    /**
     * Processes incoming user intents to update the UI state or perform specific actions.
     *
     * This function acts as the central entry point for all user interactions, delegating
     * intents to either [handleAction] for side-effect-heavy operations or [reduceState] for
     * synchronous state updates.
     *
     * @param intent The [BudgetEditorIntent] representing the user action or state change.
     */
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

            BudgetEditorIntent.Action.ChangeCategoryClicked -> handleChangeCategory()
            BudgetEditorIntent.Action.ContinueCreatingBudget -> handleContinueCreatingBudget()
            BudgetEditorIntent.Action.CancelExistingBudgetsDialog -> handleCancelExistingBudgetDialog()

            is BudgetEditorIntent.Action.DiscardDialogUpdated ->
                handleDiscardDialog(intent.action)

            is BudgetEditorIntent.Action.DeleteDialogUpdated ->
                handleDeleteDialog(intent.action)

            is BudgetEditorIntent.Action.EditExistingBudget ->
                handleEditExistingBudget(intent.budgetId)

            is BudgetEditorIntent.Action.DuplicateBudgetDialogUpdated ->
                handleDuplicateBudgetDialog(intent.action)
        }
    }

    private fun handleSave() {
        viewModelScope.launch {
            val state = uiState.value

            uiInternalState.update { it.copy(hasTriedToSave = true) }

            if (!state.isValid) {
                showSnackBar(R.string.snackBar_save_budget_error)
                return@launch
            }

            val hasDuplicate = hasExistingBudgetForPeriod(
                subcategoryId = state.subcategory.id,
                period = state.period,
                budgetId = state.budget.id.takeIf {
                    config is BudgetEditorConfig.Edit
                }
            )

            if (hasDuplicate) {
                handleDuplicateBudgetDialog(ConfirmationDialogAction.Open)
                return@launch
            }

            saveBudget()
        }
    }

    private fun saveBudget() {
        viewModelScope.launch {
            val state = uiState.value
            val isEditMode = config is BudgetEditorConfig.Edit

            val budget = state.budget.copy(
                categoryId = state.category.id,
                subcategoryId = state.subcategory.id,
                period = state.period,
                repeat = state.isRepeat,
                limitAmount = state.limitAmount
            )

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

    private fun handleDuplicateBudgetDialog(action: ConfirmationDialogAction) {
        handleConfirmationDialog(
            current = uiInternalState.value.duplicateBudgetDialog,
            action = action,
            updateState = { newDialogState ->
                uiInternalState.update { currentState ->
                    currentState.copy(duplicateBudgetDialog = newDialogState)
                }
            },
            onConfirmed = { navigateBack() }
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

    private fun handleContinueCreatingBudget() {
        val subcategoryId = uiState.value.subcategory.id

        uiInternalState.update {
            it.copy(existingBudgetsDialogDismissedFor = subcategoryId)
        }
    }

    private fun handleCancelExistingBudgetDialog() {
        navigateBack()
    }

    private fun handleEditExistingBudget(budgetId: Int) {
        val route = Route.BudgetEditor.create(
            BudgetEditorConfig.Edit(budgetId)
        )
        navigateTo(route)
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