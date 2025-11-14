package com.jorgelobo.koobe.ui.components.composed.lists

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory

@Stable
data class ListSubcategoryItemConfig(
    val subcategory: Subcategory,
    val category: Category
)