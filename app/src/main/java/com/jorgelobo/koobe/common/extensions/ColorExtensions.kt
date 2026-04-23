package com.jorgelobo.koobe.common.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import com.jorgelobo.koobe.ui.theme.color.LightThemeGrey3

fun Color.toHexString(): String {
    return String.format("#%08x", this.toArgb())
}

fun String?.toColor(): Color {
    return this
        ?.takeIf { it.isNotBlank() }
        ?.runCatching { Color(toColorInt()) }
        ?.getOrNull()
        ?: LightThemeGrey3
}