package com.jorgelobo.koobe.ui.components.composed.grids

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.ui.components.model.subcategory.SubcategoryUiModel

@Stable
data class SubcategoriesGridConfig(
    val list: List<SubcategoryUiModel> = emptyList(),
    val selectedSubcategoryId: Int? = null,
    val onSubcategoryClick: (Int) -> Unit
)