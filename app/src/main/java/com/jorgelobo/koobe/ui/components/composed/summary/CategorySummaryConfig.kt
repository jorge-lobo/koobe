package com.jorgelobo.koobe.ui.components.composed.summary

import androidx.compose.ui.graphics.Color
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class CategorySummaryConfig(
    val icon: IconPack,
    val color: Color,
    val categoryName: String,
    val subcategoryName: String? = null,
    val onChangeClick: () -> Unit
)