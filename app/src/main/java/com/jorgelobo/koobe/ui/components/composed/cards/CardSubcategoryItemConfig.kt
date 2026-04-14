package com.jorgelobo.koobe.ui.components.composed.cards

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory

@Stable
data class CardSubcategoryItemConfig(
    val subcategory: Subcategory,
    val category: Category
)