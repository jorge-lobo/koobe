package com.jorgelobo.koobe.core.model

import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.common.extensions.toColor
import com.jorgelobo.koobe.common.extensions.toHexString

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

fun FieldUpdate<Color>.resolveToHex(current: String): String {
    return resolve(current.toColor()).toHexString()
}