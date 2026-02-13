package com.jorgelobo.koobe.ui.screen.categories.selector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.constants.enums.CategoryDetailType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.reduceConfirmationDialog
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorConfig
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and user interactions
 * of the category selection flow.
 *
 * It coordinates category, subcategory, and shortcut selection
 * according to the provided [CategorySelectorConfig].
 */
@HiltViewModel
class CategorySelectorViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val subcategoryRepository: SubcategoryRepository,
    private val shortcutRepository: ShortcutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategorySelectorUiState.initialEmpty())
    val uiState: StateFlow<CategorySelectorUiState> = _uiState

    private val _events = MutableSharedFlow<CategorySelectorEvent>()
    val events = _events.asSharedFlow()

    private lateinit var config: CategorySelectorConfig

    /**
     * Initializes the ViewModel with the required configuration.
     *
     * This function must be called before any user interaction methods.
     * It should be invoked only once when the screen is created.
     * Subsequent calls are ignored.
     *
     * @param config The configuration object containing initial settings
     * for the category selector.
     */
    fun init(config: CategorySelectorConfig) {
        if (this::config.isInitialized) return
        this.config = config

        _uiState.value = CategorySelectorUiState.initial(config)

        loadCategories()
    }

    // User actions

    /**
     * Handles the user action of changing the transaction type (e.g., from Expense to Income).
     *
     * This function updates the UI state to reflect the new transaction type. It also resets
     * any previous selections for category, subcategory, and shortcut, and returns the user to the
     * initial category selection step. After updating the state, it triggers a reload of the
     * categories list to display only those relevant to the newly selected transaction type.
     *
     * @param type The new [TransactionType] selected by the user.
     */
    fun onTransactionTypeChanged(type: TransactionType) {
        _uiState.update {
            it.copy(
                transactionType = type,
                selectedCategoryId = null,
                selectedSubcategoryId = null,
                selectedShortcutId = null,
                step = SelectorStep.SelectCategory
            )
        }

        loadCategories()
    }

    /**
     * Resets the selection flow back to the category selection step,
     * allowing the user to choose a different category.
     */
    fun onChangeCategoryClick() {
        _uiState.update { it.copy(step = SelectorStep.SelectCategory) }
    }

    /**
     * Handles the user action of selecting a category.
     *
     * This function updates the UI state with the selected category ID and resets any previously
     * selected subcategory or shortcut.
     *
     * If the current configuration requires subcategory selection, the selector advances to
     * [SelectorStep.SelectSubcategory]. Otherwise, the selection is considered complete at the
     * category level.
     *
     * @param categoryId The ID of the category that was selected.
     */
    fun onCategorySelected(categoryId: Int) {
        _uiState.update {
            it.copy(
                selectedCategoryId = categoryId,
                selectedSubcategoryId = null,
                selectedShortcutId = null
            )
        }

        if (config.mode.requiresSubcategorySelection) {
            loadCategoryDetails(categoryId)
            _uiState.update { it.copy(step = SelectorStep.SelectSubcategory) }
        } else {
            updatePrimaryActionState()
        }
    }

    /**
     * Updates the UI state when a subcategory is selected and refreshes
     * the primary action button state.
     */
    fun onSubcategorySelected(subcategoryId: Int) {
        _uiState.update { it.copy(selectedSubcategoryId = subcategoryId) }
        updatePrimaryActionState()
    }

    /**
     * Updates the UI state when a shortcut is selected and refreshes
     * the primary action button state.
     */
    fun onShortcutSelected(shortcutId: Int) {
        _uiState.update { it.copy(selectedShortcutId = shortcutId) }
        updatePrimaryActionState()
    }

    /**
     * Handles the selection of a category detail type (e.g., Subcategories or Shortcuts).
     *
     * This action updates the UI state to reflect the chosen detail type and resets any previously
     * selected subcategory or shortcut to ensure a clean state for the new selection context. It then
     * updates the state of the primary action button based on the new conditions.
     *
     * @param type The type of category detail selected by the user, which can be either
     *             [CategoryDetailType.SUBCATEGORIES] or [CategoryDetailType.SHORTCUTS].
     */
    fun onCategoryDetailSelected(type: CategoryDetailType) {
        _uiState.update {
            it.copy(
                categoryDetailSelected = type,
                selectedSubcategoryId = null,
                selectedShortcutId = null
            )
        }
        updatePrimaryActionState()
    }

    /**
     * Handles the user's request to go back.
     *
     * If there are unsaved changes in the current state, it triggers the display
     * of a confirmation dialog to prevent accidental data loss. Otherwise, it emits
     * a [CategorySelectorEvent.NavigateBack] event to signal that navigation should proceed.
     */
    fun onBackRequested() {
        val state = _uiState.value

        if (state.hasUnsavedChanges) {
            onDiscardDialogAction(ConfirmationDialogAction.RequestClose)
        } else {
            navigateBack()
        }
    }

    fun onProceedRequested() {
        val state = _uiState.value

        if (!state.isPrimaryActionEnabled) return

        val route = config.target.toRoute(config, state)

        navigateAndReplace(route)
    }

    fun onSubcategoryEditorRequested() {
        val state = _uiState.value

        navigateTo(
            Route.SubcategoryEditor.create(
                SubcategoryEditorConfig(
                    subcategoryId = state.selectedSubcategoryId,
                    categoryId = state.selectedCategoryId
                )
            )
        )
    }

    fun onShortcutEditorRequested() {
        val state = _uiState.value

        navigateTo(
            Route.ShortcutEditor.create(
                ShortcutEditorConfig(
                    shortcutId = state.selectedShortcutId,
                    categoryId = state.selectedCategoryId
                )
            )
        )
    }

    fun onCreateCategoryRequested() {
        navigateTo(
            Route.CategoryEditor.create(
                CategoryEditorConfig(categoryId = null)
            )
        )
    }

    /**
     * Processes actions related to the discard changes confirmation dialog.
     *
     * This method updates the state of the discard dialog using a reducer and handles side effects
     * triggered by the user's interaction with the dialog, such as confirming the intent to
     * navigate back and discard changes.
     *
     * @param action The [ConfirmationDialogAction] to be processed (e.g., RequestClose, Confirm).
     */
    fun onDiscardDialogAction(action: ConfirmationDialogAction) {
        val (dialogState, effect) = reduceConfirmationDialog(
            state = uiState.value.discardDialog,
            action = action
        )

        _uiState.update {
            it.copy(discardDialog = dialogState)
        }

        when (effect) {
            ConfirmationDialogEffect.Confirmed -> navigateBack()

            null -> Unit
        }
    }

    // Data loading

    /**
     * Loads and filters categories according to the currently selected [TransactionType].
     *
     * Updates the UI state with a loading indicator, applies filtering and sorting,
     * and refreshes the primary action state. Errors are reflected in the UI state.
     */
    private fun loadCategories() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val allCategories = categoryRepository.getAllCategories().first()

                val filteredCategories = allCategories
                    .filter { it.type == _uiState.value.transactionType }
                    .sortedBy { it.id }

                _uiState.update {
                    it.copy(
                        categories = filteredCategories,
                        isLoading = false
                    )
                }

                updatePrimaryActionState()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    /**
     * Loads the subcategories and shortcuts associated with a given category ID from their respective
     * repositories. This is typically called after a user selects a primary category.
     * The fetched lists of subcategories and shortcuts are then used to update the UI state,
     * allowing the user to make further selections if required by the current mode.
     *
     * This operation is performed asynchronously within a `viewModelScope` coroutine.
     *
     * @param categoryId The unique identifier of the category for which to load details.
     */
    private fun loadCategoryDetails(categoryId: Int) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val subcategories =
                    subcategoryRepository.getSubcategoriesByCategoryId(categoryId).first()
                val shortcuts = shortcutRepository.getShortcutsByCategoryId(categoryId).first()

                _uiState.update {
                    it.copy(
                        subcategories = subcategories,
                        shortcuts = shortcuts,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    // State helpers

    /**
     * Updates the enabled state of the primary action button based on the current selection state.
     *
     * The button is enabled if a valid selection has been made according to the current step and configuration.
     * - In [SelectorStep.SelectCategory] step, it's enabled if a category is selected and the configuration
     *   doesn't require a subcategory selection.
     * - In [SelectorStep.SelectSubcategory] step, it's enabled if a subcategory or a shortcut is selected,
     *   depending on which detail type is currently active.
     */
    private fun updatePrimaryActionState() {
        val state = _uiState.value

        val enabled = when (state.step) {
            SelectorStep.SelectCategory -> state.selectedCategoryId != null && (!config.mode.requiresSubcategorySelection)

            SelectorStep.SelectSubcategory -> when (state.categoryDetailSelected) {
                CategoryDetailType.SUBCATEGORIES -> state.selectedSubcategoryId != null
                CategoryDetailType.SHORTCUTS -> state.selectedShortcutId != null
            }
        }

        _uiState.update { it.copy(isPrimaryActionEnabled = enabled) }
    }

    private fun emitEvent(event: CategorySelectorEvent) {
        viewModelScope.launch { _events.emit(event) }
    }

    private fun navigateTo(route: String) {
        emitEvent(CategorySelectorEvent.NavigateTo(route))
    }

    private fun navigateAndReplace(route: String) {
        emitEvent(CategorySelectorEvent.NavigateAndReplace(route))
    }

    private fun navigateBack() {
        emitEvent(CategorySelectorEvent.NavigateBack)
    }
}