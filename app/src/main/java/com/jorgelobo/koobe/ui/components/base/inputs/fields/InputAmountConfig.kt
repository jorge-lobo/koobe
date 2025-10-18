package com.jorgelobo.koobe.ui.components.base.inputs.fields

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType

data class InputAmountConfig(
    val value: Double = 0.0,
    val currencyType: CurrencyType,
    val onResetClick: () -> Unit
)