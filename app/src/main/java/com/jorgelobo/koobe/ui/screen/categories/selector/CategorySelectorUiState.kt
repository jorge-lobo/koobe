package com.jorgelobo.koobe.ui.screen.categories.selector

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.CategoryDetailType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut

data class CategorySelectorUiState(
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
    val errorMessage: String? = null
)