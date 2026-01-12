package com.jorgelobo.koobe.ui.mappers

import com.jorgelobo.koobe.domain.amount.AmountAction
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey

fun KeypadKey.toAmountAction(): AmountAction =
    when (this) {
        is KeypadKey.Digit -> AmountAction.Digit(value)
        KeypadKey.Decimal -> AmountAction.Decimal
        KeypadKey.Backspace -> AmountAction.Backspace
    }