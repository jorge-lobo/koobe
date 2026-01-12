package com.jorgelobo.koobe.domain.amount

sealed class AmountAction {
    data class Digit(val value: Int) : AmountAction()
    object Decimal : AmountAction()
    object Backspace : AmountAction()
}

fun reduceAmountInput(
    current: String,
    action: AmountAction
): String {
    return when (action) {
        is AmountAction.Digit -> appendDigit(current, action.value)
        AmountAction.Decimal -> appendDecimal(current)
        AmountAction.Backspace -> removeLast(current)
    }
}