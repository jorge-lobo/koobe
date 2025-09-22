package com.jorgelobo.koobe.domain.model.category

data class CategoryHistory(
    val category: Category,
    val transactionCount: Int,
    val totalAmount: Double,
    val subcategories: List<SubcategorySummary>
)