package com.jorgelobo.koobe.ui.mappers

import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.domain.model.transaction.DescriptionSource

@Composable
fun DescriptionSource?.asText(): String {
    return when (this) {
        DescriptionSource.Empty -> ""
        is DescriptionSource.TextDescription -> text
        null -> ""
    }
}