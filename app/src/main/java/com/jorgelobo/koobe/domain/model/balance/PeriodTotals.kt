package com.jorgelobo.koobe.domain.model.balance

data class PeriodTotals(
    val income: Double = 0.0,
    val expenses: Double = 0.0
) {
    val balance: Double
        get() = income - expenses
}