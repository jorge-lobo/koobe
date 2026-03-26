package com.jorgelobo.koobe.ui.screen.categories.manager

import com.jorgelobo.koobe.domain.model.category.Category

data class CategoryItemUi(
    val category: Category,
    val isExpanded: Boolean
)