package com.jorgelobo.koobe.common.extensions

import androidx.compose.ui.graphics.Color

fun Color.toHexString(): String {
    val r = (red * 255).toInt()
    val g = (green * 255).toInt()
    val b = (blue * 255).toInt()
    return String.format("#%02x%02x%02x", r, g, b)
}