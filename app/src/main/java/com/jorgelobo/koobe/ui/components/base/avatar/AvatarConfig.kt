package com.jorgelobo.koobe.ui.components.base.avatar

import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class AvatarConfig(
    val type: AvatarType,
    val icon: IconPack,
    val color: Color,
    val isSelected: Boolean = false
)