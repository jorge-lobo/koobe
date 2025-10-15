package com.jorgelobo.koobe.ui.components.base.avatar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType

data class AvatarConfig(
    val type: AvatarType,
    val icon: ImageVector,
    val color: Color,
    val isSelected: Boolean = false
)