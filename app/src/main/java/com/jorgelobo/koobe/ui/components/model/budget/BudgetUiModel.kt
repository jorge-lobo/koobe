package com.jorgelobo.koobe.ui.components.model.budget

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory

data class BudgetUiModel(
    val budget: Budget,
    val category: Category,
    val subcategory: Subcategory
)