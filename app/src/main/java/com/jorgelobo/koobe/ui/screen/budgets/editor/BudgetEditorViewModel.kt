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
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.settings.DefaultUserSettings
import com.jorgelobo.koobe.domain.settings.GetUserSettingsUseCase
import com.jorgelobo.koobe.domain.usecase.budget.DeleteBudgetUseCase
import com.jorgelobo.koobe.domain.usecase.budget.GetBudgetByIdUseCase
import com.jorgelobo.koobe.domain.usecase.budget.GetBudgetsBySubcategoryIdUseCase
import com.jorgelobo.koobe.domain.usecase.budget.SaveBudgetUseCase
import com.jorgelobo.koobe.domain.usecase.budget.period.GetBudgetPeriodUseCase
import com.jorgelobo.koobe.domain.usecase.budget.period.HasConflictingBudgetUseCase
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
import com.jorgelobo.koobe.utils.date.DateUtils
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
 * @param savedStateHandle Handle to saved state passed during ViewModel creation, used to retrieve navigation arguments.
 * @param getUserSettings Use case to observe user preferences (language, currency, start of week).
 * @property getBudgetById Use case to load an existing budget for editing.
 * @property getCategoryById Use case to retrieve category details for display.
 * @property getSubCategoryById Use case to retrieve subcategory details for display.
 * @property saveBudget Use case to persist or update a budget.
 * @property deleteBudget Use case to remove an existing budget.
 * @property getBudgetsBySubcategoryId Use case to check for existing budgets in the same subcategory.
 * @property hasConflictingBudget Use case to check for conflicting budget periods in the same subcategory.
 * @property getBudgetPeriod Use case to calculate period boundaries for budgets.
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
    private val hasConflictingBudget: HasConflictingBudgetUseCase,
    private val getBudgetPeriod: GetBudgetPeriodUseCase
) : ViewModel() {

    /** Editor configuration decoded from navigation arguments. */
    private val config: BudgetEditorConfig =
        savedStateHandle.get<String>("config")
            ?.let { URLDecoder.decode(it, "UTF-8") }
            ?.let { Json.decodeFromString<BudgetEditorConfig>(it) }
            ?: error("Missing BudgetEditorConfig")

    /** Internal backing flow for one-off UI events. */
    private val _events = MutableSharedFlow<BudgetEditorEvent>()

    /**
     * Emits one-off UI events such as navigation actions and snackbar messages.
     */
    val events = _events.asSharedFlow()

    /** Mutable form state representing user input changes. */
    private val formState = MutableStateFlow(BudgetFormState())

    /** Mutable internal UI state representing dialog states and operation progress. */
    private val uiInternalState = MutableStateFlow(BudgetUiStateInternal())

    /** Flow of user settings (language, currency, start of week). */
    private val userSettingsFlow: StateFlow<Triple<AppLanguage, CurrencyType, StartOfWeek>> =
        getUserSettings()
            .map { settings ->
                Triple(
                    settings.language,
                    settings.currency,
                    settings.startOfWeek
                )
            }
            .catch { exception ->
                if (exception is CancellationException) throw exception

                showSnackBar(R.string.snackBar_load_settings_error)

                emit(
                    Triple(
                        DefaultUserSettings.language,
                        DefaultUserSettings.currency,
                        DefaultUserSettings.startOfWeek
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = Triple(
                    DefaultUserSettings.language,
                    DefaultUserSettings.currency,
                    DefaultUserSettings.startOfWeek
                )
            )

    /** Initial data flow that loads budget, category, and subcategory data based on [config]. */
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

    /** Base UI state derived from combining initial editor data with user settings. */
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

    /** Flow emitting existing budgets for the selected subcategory when creating a new budget. */
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

    /**
     * Reduces synchronous state intents using [BudgetEditorReducer] and updates internal state flows.
     *
     * @param intent The state intent representing input or selection changes.
     */
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

    /**
     * Handles action intents that involve side effects, navigation, dialogs, or asynchronous operations.
     *
     * @param intent The action intent to handle.
     */
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

    /**
     * Handles the save action by validating input, checking for duplicate/conflicting budget periods,
     * and persisting the budget via [persistBudget].
     */
    private fun handleSave() {
        viewModelScope.launch {
            val state = uiState.value

            uiInternalState.update { it.copy(hasTriedToSave = true) }

            if (!state.isValid) {
                showSnackBar(R.string.snackBar_save_budget_error)
                return@launch
            }

            val startOfWeek = userSettingsFlow.value.third

            val budget = buildBudget(
                state = state,
                startOfWeek = startOfWeek
            )

            val hasConflict = try {
                hasConflictingBudget(
                    budget = budget,
                    startOfWeek = startOfWeek
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                showSnackBar(R.string.snackBar_save_budget_error)
                return@launch
            }

            if (hasConflict) {
                handleDuplicateBudgetDialog(ConfirmationDialogAction.Open)
                return@launch
            }

            persistBudget(budget)
        }
    }

    /**
     * Constructs a [Budget] domain model based on the current editor UI state and user preferences.
     *
     * Calculates the period start date depending on whether the budget is newly created,
     * being edited, or if its period type has been modified.
     *
     * @param state The current [BudgetEditorUiState] containing user inputs and configuration.
     * @param startOfWeek The user's preferred [StartOfWeek] day for calculating period boundaries.
     * @return A [Budget] domain model configured for saving or updating.
     */
    private fun buildBudget(
        state: BudgetEditorUiState,
        startOfWeek: StartOfWeek
    ): Budget {
        val isEditMode = config is BudgetEditorConfig.Edit
        val existingBudget = state.budget
        val periodChanged = isEditMode && existingBudget.period != state.period
        val periodStartDate = if (!isEditMode || periodChanged) {
            getBudgetPeriod(
                date = DateUtils.currentDate,
                periodType = state.period,
                startOfWeek = startOfWeek
            ).startDate
        } else {
            existingBudget.periodStartDate
        }

        return Budget(
            id = if (isEditMode) existingBudget.id else 0,
            categoryId = state.category.id,
            subcategoryId = state.subcategory.id,
            period = state.period,
            periodStartDate = periodStartDate,
            isActive = !isEditMode || existingBudget.isActive,
            repeat = state.isRepeat,
            limitAmount = state.limitAmount,
            spentAmount = if (isEditMode) existingBudget.spentAmount else 0.0,
            projectedAmount = if (isEditMode) existingBudget.projectedAmount else 0.0,
            dailyAverage = if (isEditMode) existingBudget.dailyAverage else 0.0
        )
    }

    /**
     * Persists the [budget] using [saveBudget] use case and updates UI saving state flags.
     *
     * @param budget The domain budget entity to save or update.
     */
    private suspend fun persistBudget(budget: Budget) {
        val isEditMode = config is BudgetEditorConfig.Edit

        uiInternalState.update { it.copy(isSaving = true) }

        runCatching {
            saveBudget(
                budget = budget,
                isEditorMode = isEditMode
            )
        }.onSuccess {
            uiInternalState.update { it.copy(isSaving = false) }
            navigateBack()
        }.onFailure { exception ->
            if (exception is CancellationException) throw exception

            uiInternalState.update { it.copy(isSaving = false) }
            showSnackBar(R.string.snackBar_save_budget_error)
        }
    }

    /**
     * Handles closing the editor screen by prompting to discard unsaved changes if any exist,
     * or navigating back immediately if no changes were made.
     */
    private fun handleClose() {
        if (formState.value.hasChanges) {
            handleDiscardDialog(ConfirmationDialogAction.Open)
        } else {
            navigateBack()
        }
    }

    /**
     * Navigates to the category selector screen to allow the user to select a new category/subcategory.
     */
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

    /**
     * Updates the discard changes dialog state based on [action].
     *
     * @param action The confirmation dialog action (e.g. Open, Dismiss, Confirm).
     */
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

    /**
     * Updates the delete budget confirmation dialog state based on [action].
     *
     * @param action The confirmation dialog action (e.g. Open, Dismiss, Confirm).
     */
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

    /**
     * Updates the duplicate budget warning dialog state based on [action].
     *
     * @param action The confirmation dialog action (e.g. Open, Dismiss, Confirm).
     */
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

    /**
     * Deletes the currently edited budget via [deleteBudget] use case and exits on success.
     */
    private fun deleteCurrentBudget() {
        val budget = uiState.value.budget

        viewModelScope.launch {
            uiInternalState.update { it.copy(isDeleting = true) }

            runCatching {
                deleteBudget(budget)
            }.onSuccess {
                uiInternalState.update { it.copy(isDeleting = false) }
                navigateBack()
            }.onFailure { exception ->
                if (exception is CancellationException) throw exception

                uiInternalState.update { it.copy(isDeleting = false) }
                showSnackBar(messageRes = R.string.snackBar_delete_budget_error)
            }
        }
    }

    /**
     * Marks the existing budgets warning dialog as dismissed for the currently selected subcategory,
     * allowing the user to continue creating the budget.
     */
    private fun handleContinueCreatingBudget() {
        val subcategoryId = uiState.value.subcategory.id

        uiInternalState.update {
            it.copy(existingBudgetsDialogDismissedFor = subcategoryId)
        }
    }

    /**
     * Cancels budget creation from the existing budgets dialog and exits the screen.
     */
    private fun handleCancelExistingBudgetDialog() {
        navigateBack()
    }

    /**
     * Navigates to edit mode for an existing budget specified by [budgetId].
     *
     * @param budgetId The ID of the existing budget to edit.
     */
    private fun handleEditExistingBudget(budgetId: Int) {
        val route = Route.BudgetEditor.create(
            BudgetEditorConfig.Edit(budgetId)
        )
        navigateTo(route)
    }

    /**
     * Emits a snackbar UI event with a string resource message.
     *
     * @param messageRes Resource ID of the string to display in the snackbar.
     */
    private fun showSnackBar(messageRes: Int) {
        emitEvent(
            BudgetEditorEvent.ShowSnackBar(
                messageRes,
                null,
                IconPack.WARNING
            )
        )
    }

    /**
     * Emits an exit event to navigate back to the previous screen.
     */
    private fun navigateBack() {
        emitEvent(BudgetEditorEvent.ExitToOrigin)
    }

    /**
     * Emits a navigation event to navigate to the specified [route].
     *
     * @param route The target destination route string.
     */
    private fun navigateTo(route: String) {
        emitEvent(BudgetEditorEvent.NavigateTo(route))
    }

    /**
     * Emits a [BudgetEditorEvent] on the [_events] flow.
     *
     * @param event The event to emit.
     */
    private fun emitEvent(event: BudgetEditorEvent) {
        viewModelScope.launch { _events.emit(event) }
    }
}