package com.jorgelobo.koobe.ui.components.composed.containers

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType

data class QuickStatsCardConfig(
    val dailyIncome: Double,
    val dailyExpenses: Double,
    val weeklyIncome: Double,
    val weeklyExpenses: Double,
    val currencyType: CurrencyType
)