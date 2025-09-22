package com.jorgelobo.koobe.domain.model.category

data class Subcategory(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val icon: String
)