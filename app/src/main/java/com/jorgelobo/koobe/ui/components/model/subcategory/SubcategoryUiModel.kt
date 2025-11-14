package com.jorgelobo.koobe.ui.components.model.subcategory

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory

data class SubcategoryUiModel(
    val subcategory: Subcategory,
    val category: Category
)