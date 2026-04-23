package com.jorgelobo.koobe.common.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt

fun Color.toHexString(): String {
    return String.format("#%08x", this.toArgb())
}

fun String.toColor(): Color =
    Color(this.toColorInt())