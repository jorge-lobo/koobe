package com.jorgelobo.koobe.ui.components.composed.lists

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

@Stable
data class ManagementListItemConfig(
    val title: String,
    val icon: IconPack,
    val color: Color
)