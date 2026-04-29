package com.jorgelobo.koobe.core.model

import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.common.extensions.toColor
import com.jorgelobo.koobe.common.extensions.toHexString
import com.jorgelobo.koobe.ui.theme.color.LightThemeGrey3

sealed class FieldUpdate<out T> {
    object Unchanged : FieldUpdate<Nothing>()
    data class Updated<T>(val value: T) : FieldUpdate<T>()
}

fun <T> FieldUpdate<T>.resolve(current: T): T {
    return when (this) {
        is FieldUpdate.Unchanged -> current
        is FieldUpdate.Updated -> value
    }
}

fun <T> updateIfChanged(newValue: T, original: T): FieldUpdate<T> {
    return if (newValue == original) FieldUpdate.Unchanged else FieldUpdate.Updated(newValue)
}

fun FieldUpdate<Color>.resolveToHex(current: String): String {
    val baseColor = runCatching { current.toColor() }.getOrDefault(LightThemeGrey3)
    return resolve(baseColor).toHexString()
}