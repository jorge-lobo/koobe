package com.jorgelobo.koobe.ui.mappers

import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.domain.model.transaction.DescriptionSource

/**
 * Converts a [DescriptionSource] into a displayable text.
 *
 * Returns an empty string when the source is [DescriptionSource.Empty] or null, making it safe
 * to use directly in UI text fields.
 */
@Composable
fun DescriptionSource?.asText(): String {
    return when (this) {
        DescriptionSource.Empty -> ""
        is DescriptionSource.TextDescription -> text
        null -> ""
    }
}