package com.jorgelobo.koobe.ui.screen.categories.selector

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.CategoryDetailType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.R

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
    val showDiscardDialog: Boolean = false
) {
    val hasUnsavedChanges: Boolean
        get() = transactionType != initialSnapshot.transactionType ||
                selectedCategoryId != initialSnapshot.categoryId ||
                selectedSubcategoryId != initialSnapshot.subcategoryId ||
                selectedShortcutId != initialSnapshot.shortcutId

    val headlineRes: Int
        get() = when (step) {
            SelectorStep.SelectCategory -> mode.headlineRes
            SelectorStep.SelectSubcategory -> when (categoryDetailSelected) {
                CategoryDetailType.SUBCATEGORIES -> R.string.headline_subcategory_selector
                CategoryDetailType.SHORTCUTS -> R.string.headline_shortcut_selector
            }
        }

    companion object {
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

data class InitialSnapshot(
    val transactionType: TransactionType,
    val categoryId: Int?,
    val subcategoryId: Int?,
    val shortcutId: Int?
)