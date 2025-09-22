package com.jorgelobo.koobe.domain.model.category

import com.jorgelobo.koobe.domain.model.constants.TransactionType

data class Category(
    val id: Int,
    val name: String,
    val icon: String,
    val color: String,
    val type: TransactionType,
    val subcategories: List<Subcategory> = emptyList()
)