package com.jorgelobo.koobe.domain.model.category

import androidx.compose.ui.graphics.vector.ImageVector

data class Subcategory(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val icon: ImageVector
)