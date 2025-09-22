package com.jorgelobo.koobe.domain.model.chart

data class ChartSlice(
    val id: Int,
    val label: String,
    val icon: String?,
    val color: String?,
    val amount: Double,
    val percentage: Double
)