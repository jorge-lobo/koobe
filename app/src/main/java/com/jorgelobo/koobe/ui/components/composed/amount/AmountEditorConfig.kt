package com.jorgelobo.koobe.ui.components.composed.amount

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class AmountEditorConfig(
    val paymentIcon: IconPack,
    val currencyType: CurrencyType,
    val value: Double,
    val onResetClick: () -> Unit,
    val onPaymentSelectorClick: () -> Unit,
    val onCurrencySelectorClick: () -> Unit,
    val onKeyClick: (KeypadKey) -> Unit
)