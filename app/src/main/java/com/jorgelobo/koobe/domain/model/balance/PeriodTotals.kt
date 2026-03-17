package com.jorgelobo.koobe.domain.model.balance

data class PeriodTotals(
    val income: Double,
    val expenses: Double
) {
    val balance: Double
        get() = income - expenses
}