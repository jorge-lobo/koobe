package com.jorgelobo.koobe.ui.components.composed.grids

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.category.Category

@Stable
data class CategoriesGridConfig(
    val list: List<Category> = emptyList(),
    val selectedCategoryId: Int? = null ,
    val onCategoryClick: (Int) -> Unit
)