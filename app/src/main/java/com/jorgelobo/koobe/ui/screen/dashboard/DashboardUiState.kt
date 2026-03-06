package com.jorgelobo.koobe.ui.screen.dashboard

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.components.model.shortcut.ShortcutUiModel
import com.jorgelobo.koobe.utils.date.DateUtils
import java.util.Date

data class DashboardUiState(
    val date: Date = DateUtils.currentDate,
    val startOfWeek: StartOfWeek = StartOfWeek.SUNDAY,
    val currencyType: CurrencyType = CurrencyType.EUR,
    val overallBalance: Double = 0.0,
    val income: Double = 0.0,
    val expenses: Double = 0.0,
    val dailyExpenses: Double = 0.0,
    val dailyIncome: Double = 0.0,
    val weeklyExpenses: Double = 0.0,
    val weeklyIncome: Double = 0.0,
    val budgetItems: List<BudgetUiModel> = emptyList(),
    val shortcutItems: List<ShortcutUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)