package com.jorgelobo.koobe.ui.components.base.inputs.fields

data class InputAmountConfig(
    val value: Double = 0.0,
    val currencySymbol: String,
    val onResetClick: () -> Unit
)