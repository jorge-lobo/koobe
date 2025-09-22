package com.jorgelobo.koobe.domain.model.chart

import java.time.LocalDate

data class BalancePoint(
    val date: LocalDate,
    val balance: Double
)