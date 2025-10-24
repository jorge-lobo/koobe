package com.jorgelobo.koobe.ui.components.composed.budgets

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory

@Stable
data class BudgetItemConfig(
    val budget: Budget,
    val category: Category,
    val subcategory: Subcategory,
    val onClick: (() -> Unit)? = null
)