package com.jorgelobo.koobe.ui.screen.budgets.manager

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.ui.screen.budgets.manager.model.PeriodicBudgetsUiModel

data class BudgetManagerUiState(
    val currencyType: CurrencyType = CurrencyType.EUR,
    val balance: Double = 0.0,
    val income: Double = 0.0,
    val expenses: Double = 0.0,
    val periodicBudgets: List<PeriodicBudgetsUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)