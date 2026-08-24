package com.jorgelobo.koobe.domain.model.category

import com.jorgelobo.koobe.domain.model.shortcut.ShortcutHistory
import com.jorgelobo.koobe.domain.model.subcategory.SubcategoryHistory

data class CategoryHistory(
    val category: Category,
    val transactionCount: Int,
    val totalAmount: Double,
    val subcategories: List<SubcategoryHistory> = emptyList(),
    val shortcuts: List<ShortcutHistory> = emptyList()
)