package com.jorgelobo.koobe.ui.components.model.budget

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory

data class BudgetUiModel(
    val budget: Budget,
    val category: Category,
    val subcategory: Subcategory
) {
    companion object {
        fun from(budget: Budget, category: Category, subcategory: Subcategory): BudgetUiModel {
            return BudgetUiModel(
                budget = budget,
                category = category,
                subcategory = subcategory
            )
        }
    }
}