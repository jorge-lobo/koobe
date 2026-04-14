package com.jorgelobo.koobe.ui.components.composed.emptyState

import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.ui.components.model.enums.EmptyStateIconType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class EmptyStateContentConfig(
    val message: String,
    val icon: IconPack,
    val iconTint: Color,
    val iconType: EmptyStateIconType
)