package com.jorgelobo.koobe.ui.components.model.grid

import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class GridItemUiModel(
    val id: Int,
    val name: String,
    val icon: IconPack,
    val color: Color,
)