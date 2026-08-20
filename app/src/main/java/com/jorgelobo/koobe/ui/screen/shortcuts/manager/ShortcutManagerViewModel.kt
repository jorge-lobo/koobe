package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.SortingType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.usecase.category.GetAllCategoriesUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.DeleteShortcutUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.GetAllShortcutsByTypeUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.GetShortcutByIdUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.UpdateShortcutUseCase
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorTarget
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.handleSelectorSheet
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.shortcutRecurrence.ShortcutRecurrenceBottomSheetAction
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.shortcutRecurrence.reduceShortcutRecurrenceBottomSheet
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.handleConfirmationDialog
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Shortcut Manager screen.
 *
 * Manages the state and business logic for displaying, sorting, and deleting transaction
 * shortcuts. It coordinates shortcut and category data, exposes UI state, and emits one-off
 * events such as navigation.
 *
 * @property getAllShortcuts Use case to retrieve shortcuts filtered by [TransactionType].
 * @property getAllCategories Use case to fetch all categories for mapping shortcuts to their UI representation.
 * @property getShortcutById Use case to retrieve a specific shortcut by its unique identifier.
 * @property deleteShortcut Use case to remove an existing shortcut.
 * @property updateShortcut Use case to update an existing shortcut.
 */
@HiltViewModel
class ShortcutManagerViewModel @Inject constructor(
    private val getAllShortcuts: GetAllShortcutsByTypeUseCase,
    private val getAllCategories: GetAllCategoriesUseCase,
    private val getShortcutById: GetShortcutByIdUseCase,
    private val deleteShortcut: DeleteShortcutUseCase,
    private val updateShortcut: UpdateShortcutUseCase,
) : ViewModel() {

    /**
     * Internal mutable state flow that holds the current [ShortcutManagerUiState].
     * This represents the single source of truth for the screen's state.
     */
    private val _uiState = MutableStateFlow(
        ShortcutManagerUiState(
            transactionTypeSelected = TransactionType.EXPENSE,
            isLoading = true
        )
    )

    /**
     * Immutable UI state exposed to the screen.
     */
    val uiState: StateFlow<ShortcutManagerUiState> = _uiState.asStateFlow()

    /**
     * A private [MutableSharedFlow] used to emit one-time events, such as navigation or
     * displaying transient UI messages.
     */
    private val _events = MutableSharedFlow<ShortcutManagerEvent>()

    /**
     * Emits one-off UI events such as navigation.
     */
    val events = _events.asSharedFlow()

    init {
        collectShortcuts()
    }

    /**
     * Collects and combines shortcut and category data based on the current transaction type
     * and sorting selection, updating the UI state reactively.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectShortcuts() {
        viewModelScope.launch {
            _uiState
                .map { it.transactionTypeSelected to it.sortingSelector.selected }
                .distinctUntilChanged()
                .flatMapLatest { (type, sorting) ->

                    updateState {
                        copy(
                            transactionTypeSelected = type,
                            isLoading = true,
                            errorMessage = null
                        )
                    }

                    combine(
                        getAllShortcuts(type),
                        getAllCategories()
                    ) { shortcuts, categories ->

                        val categoriesById = categories.associateBy(Category::id)

                        shortcuts.mapNotNull { shortcut ->

                            val category = categoriesById[shortcut.categoryId]
                                ?: return@mapNotNull null

                            ShortcutItemUi(shortcut, category)
                        }
                            .sortedBy(sorting)
                    }
                        .catch { error ->

                            updateState {
                                copy(
                                    isLoading = false,
                                    errorMessage = error.message
                                )
                            }

                            emit(emptyList())
                        }
                }
                .collect { shortcutItems ->

                    updateState {
                        copy(
                            shortcutItems = shortcutItems,
                            isLoading = false
                        )
                    }
                }
        }
    }

    /**
     * Updates the selected transaction type and triggers a reload of the shortcut list.
     *
     * @param type The new [TransactionType] to filter the shortcuts by.
     */
    fun onTransactionTypeChange(type: TransactionType) {
        updateState {
            copy(
                transactionTypeSelected = type,
                isLoading = true
            )
        }
    }

    fun onBackClick() {
        navigateBack()
    }

    fun onSortingClick() {
        updateState {
            copy(sortingSelector = sortingSelector.copy(visible = true))
        }
    }

    /**
     * Handles the click event for adding a new shortcut.
     *
     * Triggers navigation to the category selector screen, passing a configuration that specifies
     * the shortcut creation mode and the currently selected transaction type.
     */
    fun onAddShortcutClick() {
        val route = Route.CategorySelector.create(
            CategorySelectorConfig(
                mode = CategorySelectorMode.CREATE_SHORTCUT,
                target = CategorySelectorTarget.SHORTCUT_EDITOR,
                initialTransactionType = uiState.value.transactionTypeSelected
            )
        )
        navigateTo(route)
    }

    /**
     * Handles the click event for a shortcut chip, opening the recurrence configuration sheet.
     *
     * It locates the shortcut item within the current UI state and triggers the display of the
     * bottom sheet to manage the shortcut's recurrence settings.
     *
     * @param shortcutId The unique identifier of the clicked shortcut.
     */
    fun onShortcutChipClick(shortcutId: Int) {
        val item = uiState.value.shortcutItems.find { it.shortcut.id == shortcutId } ?: return

        onShortcutRecurrenceAction(
            ShortcutRecurrenceBottomSheetAction.Open(
                shortcut = item.shortcut,
                category = item.category
            )
        )
    }

    /**
     * Triggers navigation to the shortcut editor screen to modify an existing shortcut.
     *
     * @param shortcutId The unique identifier of the shortcut to be edited.
     */
    fun onEditShortcut(shortcutId: Int) {
        val route = Route.ShortcutEditor.create(
            ShortcutEditorConfig.Edit(shortcutId)
        )
        navigateTo(route)
    }

    /**
     * Handles the click event for deleting a shortcut.
     *
     * It updates the UI state to display a confirmation dialog, storing the ID of the
     * shortcut intended for deletion.
     *
     * @param shortcutId The unique identifier of the shortcut to be deleted.
     */
    fun onDeleteShortcutClick(shortcutId: Int) {
        updateState {
            copy(
                deleteDialog = deleteDialog.copy(
                    visible = true,
                    targetId = shortcutId
                )
            )
        }
    }

    /**
     * Handles actions dispatched from the shortcut deletion confirmation dialog.
     *
     * This method coordinates the dialog state transitions based on the user's input and
     * triggers the shortcut deletion process if the confirmation action is received.
     *
     * @param action The specific [ConfirmationDialogAction] performed by the user.
     */
    fun onDeleteDialogAction(action: ConfirmationDialogAction) {
        handleConfirmationDialog(
            current = uiState.value.deleteDialog,
            action = action,
            updateState = { newState ->
                updateState {
                    copy(deleteDialog = newState)
                }
            },
            onConfirmed = { performDeleteShortcut() }
        )
    }

    /**
     * Handles actions from the sorting bottom sheet, such as selecting a [SortingType],
     * dismissing the sheet, or applying the selection.
     *
     * This method updates the UI state's sorting selector using the provided [action] via the
     * [handleSelectorSheet] utility.
     *
     * @param action The action performed within the sorting selector sheet.
     */
    fun onSortingSheetAction(action: SelectorSheetAction<SortingType>) {
        handleSelectorSheet(
            current = uiState.value.sortingSelector,
            action = action,
            updateState = { newState ->
                updateState {
                    copy(sortingSelector = newState)
                }
            },
            onApplied = { }
        )
    }

    /**
     * Handles actions from the period selector bottom sheet, which is used to define the recurrence
     * frequency of a shortcut.
     *
     * Depending on the [action], it either updates the shortcut's frequency with a new [PeriodType]
     * or dismisses the selector and clears the current target.
     *
     * @param action The specific [SelectorSheetAction] performed by the user within the period selector.
     */
    fun onPeriodSelectorAction(action: SelectorSheetAction<PeriodType>) {
        when (action) {
            is SelectorSheetAction.Select -> {
                updateShortcutFrequency(action.item)
            }

            is SelectorSheetAction.Dismiss -> {
                updateState {
                    copy(
                        periodSelector = periodSelector.copy(visible = false),
                        shortcutRecurrenceTarget = null
                    )
                }
            }

            else -> Unit
        }
    }

    /**
     * Handles actions related to the shortcut recurrence bottom sheet.
     *
     * This method updates the UI state by reducing the provided [action] to determine the
     * visibility and content of the bottom sheet. It also coordinates side effects such as
     * initiating a frequency change or disabling the recurrence for a specific shortcut.
     *
     * @param action The [ShortcutRecurrenceBottomSheetAction] to be processed.
     */
    fun onShortcutRecurrenceAction(action: ShortcutRecurrenceBottomSheetAction) {
        _uiState.update {
            it.copy(shortcutRecurrenceSheet = reduceShortcutRecurrenceBottomSheet(action))
        }

        when (action) {
            is ShortcutRecurrenceBottomSheetAction.Change -> changeRecurrenceFrequency(action.shortcut)
            is ShortcutRecurrenceBottomSheetAction.Disable -> disableRecurrence(action.shortcut)
            else -> Unit
        }
    }

    /**
     * Executes the deletion of the shortcut currently targeted by the deletion confirmation dialog.
     *
     * This method retrieves the shortcut ID from the UI state, fetches the corresponding
     * shortcut entity, and invokes the [deleteShortcut] use case within the [viewModelScope].
     * If the deletion fails, the UI state is updated with the resulting error message.
     */
    private fun performDeleteShortcut() {
        viewModelScope.launch {
            val id = uiState.value.deleteDialog.targetId ?: return@launch
            val shortcut = getShortcutById(id) ?: return@launch

            runCatching {
                deleteShortcut(shortcut)
            }.onFailure { error ->
                updateState {
                    copy(errorMessage = error.message)
                }
            }
        }
    }

    /**
     * Prepares the UI state to change the recurrence frequency of a specific shortcut.
     *
     * This method sets the provided [shortcut] as the current target for recurrence modification
     * and displays the period selector, pre-selecting the shortcut's current recurrence period.
     *
     * @param shortcut The [Shortcut] for which the recurrence frequency is being changed.
     */
    private fun changeRecurrenceFrequency(shortcut: Shortcut) {
        val period = shortcut.period ?: return

        updateState {
            copy(
                shortcutRecurrenceTarget = shortcut,
                periodSelector = periodSelector.copy(
                    visible = true,
                    selected = period
                )
            )
        }
    }

    private fun disableRecurrence(shortcut: Shortcut) {

    }

    /**
     * Updates the recurrence frequency of a specific shortcut and persists the change.
     *
     * This method takes the currently targeted shortcut from the UI state, applies the new
     * [PeriodType], and invokes the update use case. Upon completion or failure, it hides the
     * period selector and clears the recurrence target.
     *
     * @param period The new [PeriodType] to be assigned to the targeted shortcut.
     */
    private fun updateShortcutFrequency(period: PeriodType) {
        val shortcut = uiState.value.shortcutRecurrenceTarget ?: return

        viewModelScope.launch {
            runCatching {
                updateShortcut(
                    shortcut.copy(
                        period = period
                    )
                )
            }.onFailure { error ->
                updateState {
                    copy(errorMessage = error.message)
                }
            }

            updateState {
                copy(
                    periodSelector = periodSelector.copy(visible = false),
                    shortcutRecurrenceTarget = null
                )
            }
        }
    }

    private fun navigateTo(route: String) {
        emitEvent(ShortcutManagerEvent.NavigateTo(route))
    }

    private fun navigateBack() {
        emitEvent(ShortcutManagerEvent.NavigateBack)
    }

    private fun emitEvent(event: ShortcutManagerEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    private fun updateState(reducer: ShortcutManagerUiState.() -> ShortcutManagerUiState) {
        _uiState.update { it.reducer() }
    }
}