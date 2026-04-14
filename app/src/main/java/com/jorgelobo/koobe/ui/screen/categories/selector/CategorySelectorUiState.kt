package com.jorgelobo.koobe.ui.screen.categories.selector

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.CategoryDetailType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState

/**
 * Represents the full UI state of the Category Selector screen.
 *
 * This data class is used by the ViewModel to expose the current state to the composable UI.
 * It contains:
 * - the current step of the selection flow ([step])
 * - all selectable items ([categories], [subcategories], [shortcuts])
 * - the currently selected IDs
 * - derived UI state like [isPrimaryActionEnabled] or [headlineRes]
 * - dialog state holders such as [discardDialog]
 *
 * The state is immutable; all updates produce a new copy via the ViewModel.
 */
data class CategorySelectorUiState(
    val mode: CategorySelectorMode,
    val step: SelectorStep = SelectorStep.SelectCategory,
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val categories: List<Category> = emptyList(),
    val subcategories: List<Subcategory> = emptyList(),
    val shortcuts: List<Shortcut> = emptyList(),
    val selectedCategoryId: Int? = null,
    val selectedSubcategoryId: Int? = null,
    val selectedShortcutId: Int? = null,
    val categoryDetailSelected: CategoryDetailType = CategoryDetailType.SUBCATEGORIES,
    val isPrimaryActionEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val initialSnapshot: InitialSnapshot,
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState()
) {
    /**
     * Determines if the current selection differs from the initial snapshot.
     *
     * Used to prompt the user with a discard dialog when navigating back if there are unsaved changes.
     */
    val hasUnsavedChanges: Boolean
        get() = transactionType != initialSnapshot.transactionType ||
                selectedCategoryId != initialSnapshot.categoryId ||
                selectedSubcategoryId != initialSnapshot.subcategoryId ||
                selectedShortcutId != initialSnapshot.shortcutId

    /**
     * Returns the string resource for the current screen headline based on the current step
     * and selected category detail type.
     *
     * This allows the UI to dynamically update the title as the user progresses through the
     * selector flow.
     */
    val headlineRes: Int
        get() = when (step) {
            SelectorStep.SelectCategory -> mode.headlineRes
            SelectorStep.SelectSubcategory -> when (categoryDetailSelected) {
                CategoryDetailType.SUBCATEGORIES -> R.string.headline_subcategory_selector
                CategoryDetailType.SHORTCUTS -> R.string.headline_shortcut_selector
            }
        }

    companion object {
        /**
         * Returns an empty initial state with default mode and empty selections.
         * Useful for previews or initializing the ViewModel before config is applied.
         */
        fun initialEmpty() = CategorySelectorUiState(
            mode = CategorySelectorMode.DEFAULT,
            transactionType = TransactionType.EXPENSE,
            selectedCategoryId = null,
            selectedSubcategoryId = null,
            selectedShortcutId = null,
            initialSnapshot = InitialSnapshot(
                transactionType = TransactionType.EXPENSE,
                categoryId = null,
                subcategoryId = null,
                shortcutId = null
            )
        )

        /**
         * Returns the initial state based on the provided [CategorySelectorConfig], pre-selecting
         * any IDs and transaction type supplied in the config.
         */
        fun initial(config: CategorySelectorConfig) = CategorySelectorUiState(
            mode = config.mode,
            transactionType = config.initialTransactionType,
            selectedCategoryId = config.initialCategoryId,
            selectedSubcategoryId = config.initialSubcategoryId,
            selectedShortcutId = config.initialShortcutId,
            initialSnapshot = InitialSnapshot(
                transactionType = config.initialTransactionType,
                categoryId = config.initialCategoryId,
                subcategoryId = config.initialSubcategoryId,
                shortcutId = config.initialShortcutId
            )
        )
    }
}

/**
 * Captures the initial state of selections when the screen is first opened.
 *
 * Used by [CategorySelectorUiState.hasUnsavedChanges] to detect modifications made by the user.
 */
data class InitialSnapshot(
    val transactionType: TransactionType,
    val categoryId: Int?,
    val subcategoryId: Int?,
    val shortcutId: Int?
)