package com.jorgelobo.koobe.ui.components.base.numericKeypad

sealed class KeypadKey {
    data class Digit(val value: Int) : KeypadKey()
    object Decimal : KeypadKey()
    object Backspace : KeypadKey()
}

val keypadKeys = listOf(
    listOf(
        KeypadKey.Digit(1),
        KeypadKey.Digit(2),
        KeypadKey.Digit(3)
    ),
    listOf(
        KeypadKey.Digit(4),
        KeypadKey.Digit(5),
        KeypadKey.Digit(6)
    ),
    listOf(
        KeypadKey.Digit(7),
        KeypadKey.Digit(8),
        KeypadKey.Digit(9)
    ),
    listOf(
        KeypadKey.Decimal,
        KeypadKey.Digit(0),
        KeypadKey.Backspace
    )
)