package com.jorgelobo.koobe.domain.model.subcategory

fun Subcategory.isProtected(): Boolean {
    return id == PlaceholderSubcategories.EXPENSE_ID ||
            id == PlaceholderSubcategories.INCOME_ID
}