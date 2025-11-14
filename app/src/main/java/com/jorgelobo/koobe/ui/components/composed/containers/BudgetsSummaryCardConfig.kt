package com.jorgelobo.koobe.ui.components.composed.containers

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel

@Stable
data class BudgetsSummaryCardConfig(
    val items: List<BudgetUiModel> = emptyList(),
    val onBudgetClick: (BudgetUiModel) -> Unit,
    val onActionClick: () -> Unit
)