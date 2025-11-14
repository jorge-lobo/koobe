package com.jorgelobo.koobe.ui.components.composed.emptyState

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.ui.components.model.enums.EmptyStateIconType

data class EmptyStateContentConfig(
    val message: String,
    val icon: ImageVector,
    val iconTint: Color,
    val iconType: EmptyStateIconType
)