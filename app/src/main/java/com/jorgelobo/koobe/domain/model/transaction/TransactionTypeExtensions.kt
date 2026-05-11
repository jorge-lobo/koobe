package com.jorgelobo.koobe.domain.model.transaction

import com.jorgelobo.koobe.domain.model.category.PlaceholderCategories
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.subcategory.PlaceholderSubcategories

fun TransactionType.toPlaceholderIds(): Pair<Int, Int> =
    when (this) {
        TransactionType.EXPENSE -> PlaceholderCategories.EXPENSE_ID to PlaceholderSubcategories.EXPENSE_ID
        TransactionType.INCOME -> PlaceholderCategories.INCOME_ID to PlaceholderSubcategories.INCOME_ID
    }