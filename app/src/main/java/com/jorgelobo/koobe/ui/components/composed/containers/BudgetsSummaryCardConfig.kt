package com.jorgelobo.koobe.ui.components.composed.containers

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel

@Stable
data class BudgetsSummaryCardConfig(
    val items: List<BudgetUiModel> = emptyList(),
    val currencyType: CurrencyType,
    val onBudgetClick: (BudgetUiModel) -> Unit,
    val onActionClick: () -> Unit
)