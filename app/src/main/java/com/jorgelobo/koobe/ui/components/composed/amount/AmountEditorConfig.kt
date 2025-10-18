package com.jorgelobo.koobe.ui.components.composed.amount

import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey

data class AmountEditorConfig(
    val paymentIcon: ImageVector,
    val currencyType: CurrencyType,
    val value: Double,
    val onResetClick: () -> Unit,
    val onPaymentSelectorClick: () -> Unit,
    val onCurrencySelectorClick: () -> Unit,
    val onKeyClick: (KeypadKey) -> Unit
)