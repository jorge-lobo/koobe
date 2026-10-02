package com.jorgelobo.koobe.ui.components.composed.cards

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.screen.budgets.manager.model.PeriodicBudgetsUiModel

@Stable
data class CardPeriodicBudgetsConfig(
    val model: PeriodicBudgetsUiModel,
    val onExpandToggle: () -> Unit,
    val onItemClick: (BudgetUiModel) -> Unit
)