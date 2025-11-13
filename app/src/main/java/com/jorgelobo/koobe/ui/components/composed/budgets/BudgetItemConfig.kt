package com.jorgelobo.koobe.ui.components.composed.budgets

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel

@Stable
data class BudgetItemConfig(
    val model: BudgetUiModel,
    val onClick: (() -> Unit)? = null
)