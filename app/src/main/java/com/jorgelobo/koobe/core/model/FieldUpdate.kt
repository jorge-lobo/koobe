package com.jorgelobo.koobe.core.model

import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.common.extensions.toColor
import com.jorgelobo.koobe.common.extensions.toHexString
import com.jorgelobo.koobe.ui.theme.color.LightThemeGrey3

/**
 * Represents the edit state of a single form field.
 *
 * [Unchanged] means the user has not modified the field; the original persisted value is used.
 * [Updated] means the user has set an explicit value, which takes precedence over the original.
 *
 * This distinction allows the form to track only what changed, without requiring a full copy of
 * the persisted entity.
 */
sealed class FieldUpdate<out T> {
    object Unchanged : FieldUpdate<Nothing>()
    data class Updated<T>(val value: T) : FieldUpdate<T>()
}

/**
 * Returns the updated value if the field was changed, or [current] if unchanged.
 */
fun <T> FieldUpdate<T>.resolve(current: T): T {
    return when (this) {
        is FieldUpdate.Unchanged -> current
        is FieldUpdate.Updated -> value
    }
}

// Returns Unchanged if newValue equals original; Updated otherwise.
fun <T> updateIfChanged(newValue: T, original: T): FieldUpdate<T> {
    return if (newValue == original) FieldUpdate.Unchanged else FieldUpdate.Updated(newValue)
}

// Resolves the field to a hex string, falling back to LightThemeGrey3 if the base color is invalid.
fun FieldUpdate<Color>.resolveToHex(current: String): String {
    val baseColor = runCatching { current.toColor() }.getOrDefault(LightThemeGrey3)
    return resolve(baseColor).toHexString()
}