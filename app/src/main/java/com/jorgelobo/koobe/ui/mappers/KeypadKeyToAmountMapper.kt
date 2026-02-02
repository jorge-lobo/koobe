package com.jorgelobo.koobe.ui.mappers

import com.jorgelobo.koobe.domain.amount.AmountAction
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey

/**
 * Maps a [KeypadKey] pressed by the user to a corresponding [AmountAction].
 *
 * - Digits are mapped to [AmountAction.Digit] with the same value.
 * - Decimal point is mapped to [AmountAction.Decimal].
 * - Backspace is mapped to [AmountAction.Backspace].
 */
fun KeypadKey.toAmountAction(): AmountAction =
    when (this) {
        is KeypadKey.Digit -> AmountAction.Digit(value)
        KeypadKey.Decimal -> AmountAction.Decimal
        KeypadKey.Backspace -> AmountAction.Backspace
    }