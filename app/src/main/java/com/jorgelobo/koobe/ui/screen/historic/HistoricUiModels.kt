package com.jorgelobo.koobe.ui.screen.historic

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.CategoryHistory

data class CategoryHistoricUi(
    val category: Category,
    val history: CategoryHistory,
    val isExpanded: Boolean = false,
    val expandedSubcategories: Set<Int> = emptySet()
)