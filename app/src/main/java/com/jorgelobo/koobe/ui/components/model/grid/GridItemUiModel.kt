package com.jorgelobo.koobe.ui.components.model.grid

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class GridItemUiModel(
    val id: Int,
    val name: String,
    val icon: ImageVector,
    val color: Color,
)