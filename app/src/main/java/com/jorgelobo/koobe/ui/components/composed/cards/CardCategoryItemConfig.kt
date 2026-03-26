package com.jorgelobo.koobe.ui.components.composed.cards

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.category.Category

@Stable
data class CardCategoryItemConfig(
    val category: Category,
    val isExpanded: Boolean,
    val onCategoryExpandToggle: () -> Unit
)