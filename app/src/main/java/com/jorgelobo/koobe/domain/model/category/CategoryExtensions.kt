package com.jorgelobo.koobe.domain.model.category

fun Category.isProtected(): Boolean {
    return id == PlaceholderCategories.EXPENSE_ID ||
            id == PlaceholderCategories.INCOME_ID
}