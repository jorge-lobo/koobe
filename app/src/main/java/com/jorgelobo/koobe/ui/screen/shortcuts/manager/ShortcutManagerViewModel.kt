package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.SortingType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.usecase.category.GetAllCategoriesUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.DeleteShortcutUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.GetAllShortcutsByTypeUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.GetShortcutByIdUseCase
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorTarget
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.handleSelectorSheet
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
 */
@HiltViewModel
class ShortcutManagerViewModel @Inject constructor(
    private val getAllShortcuts: GetAllShortcutsByTypeUseCase,
    private val getAllCategories: GetAllCategoriesUseCase,
    private val getShortcutById: GetShortcutByIdUseCase,
    private val deleteShortcut: DeleteShortcutUseCase
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