package com.jorgelobo.koobe.ui.components.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CategorySummaryConfig(
    val icon: ImageVector,
    val color: Color,
    val categoryName: String,
    val subcategoryName: String? = null,
    val onChangeClick: () -> Unit
)