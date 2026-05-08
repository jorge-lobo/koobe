package com.jorgelobo.koobe.common.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import com.jorgelobo.koobe.ui.theme.color.LightThemeGrey3

fun Color.toHexString(): String {
    return "#%08x".format(toArgb())
}

fun String?.toColor(): Color {
    val defaultColor = LightThemeGrey3

    if (this.isNullOrBlank()) return defaultColor

    return try {
        Color(this.toColorInt())
    } catch (_: Exception) {
        try {
            val colorLong = this.removePrefix("#").toULong(16).toLong()
            Color(colorLong)
        } catch (_: Exception) {
            defaultColor
        }
    }
}