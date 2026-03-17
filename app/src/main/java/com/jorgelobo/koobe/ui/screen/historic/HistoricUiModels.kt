package com.jorgelobo.koobe.ui.screen.historic

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.CategoryHistory

/**
 * UI model representing a category and its associated historical data for the historic screen.
 *
 * @property category The [Category] domain model containing basic category information.
 * @property history The [CategoryHistory] containing the historical records and statistics for this category.
 * @property isExpanded Boolean flag indicating if the category item is currently expanded in the UI.
 * @property expandedSubcategories A set of IDs for subcategories that are currently expanded within this category's view.
 */
data class CategoryHistoricUi(
    val category: Category,
    val history: CategoryHistory,
    val isExpanded: Boolean = false,
    val expandedSubcategories: Set<Int> = emptySet()
)