package com.jorgelobo.koobe.ui.components.base.numericKeypad

import androidx.annotation.StringRes
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.model.enums.KeyType

data class KeypadKey(
    val type: KeyType,
    @field:StringRes val labelRes: Int? = null
)

val keypadKeys = listOf(
    listOf(
        KeypadKey(KeyType.NUMERIC, R.string.keypad_1),
        KeypadKey(KeyType.NUMERIC, R.string.keypad_2),
        KeypadKey(KeyType.NUMERIC, R.string.keypad_3)
    ),
    listOf(
        KeypadKey(KeyType.NUMERIC, R.string.keypad_4),
        KeypadKey(KeyType.NUMERIC, R.string.keypad_5),
        KeypadKey(KeyType.NUMERIC, R.string.keypad_6)
    ),
    listOf(
        KeypadKey(KeyType.NUMERIC, R.string.keypad_7),
        KeypadKey(KeyType.NUMERIC, R.string.keypad_8),
        KeypadKey(KeyType.NUMERIC, R.string.keypad_9)
    ),
    listOf(
        KeypadKey(KeyType.NUMERIC, R.string.keypad__),
        KeypadKey(KeyType.NUMERIC, R.string.keypad_0),
        KeypadKey(KeyType.BACKSPACE, null)
    )
)