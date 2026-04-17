package com.jorgelobo.koobe.core.model

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