package com.jorgelobo.koobe.ui.screen.budgets.manager.model

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel

data class PeriodicBudgetsUiModel(
    val periodType: PeriodType,
    val currencyType: CurrencyType,
    val budgets: List<BudgetUiModel>,
    val totalLimit: Double,
    val totalSpent: Double,
    val isExpanded: Boolean = false
) {
    val budgetsCount: Int
        get() = budgets.size

    val balance: Double
        get() = totalLimit - totalSpent

    val percentage: Double
        get() = if (totalLimit > 0) {
            (totalSpent / totalLimit) * 100
        } else {
            0.0
        }

    val progress: Float
        get() = percentage.toFloat() / 100
}