package com.jorgelobo.koobe.ui.components.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class AvatarType { SMALL, MEDIUM, LARGE, EXTRA_LARGE }

data class AvatarConfig(
    val type: AvatarType,
    val icon: ImageVector,
    val color: Color,
    val isSelected: Boolean = false
)