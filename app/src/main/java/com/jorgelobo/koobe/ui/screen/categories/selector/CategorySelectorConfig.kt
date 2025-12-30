package com.jorgelobo.koobe.ui.screen.categories.selector

import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import kotlinx.serialization.Serializable

@Serializable
data class CategorySelectorConfig(
    val mode: CategorySelectorMode,
    val target: CategorySelectorTarget,
    val transactionId: Int? = null,
    val subcategoryId: Int? = null,
    val shortcutId: Int? = null,
    val budgetId: Int? = null,
    val initialTransactionType: TransactionType,
    val initialCategoryId: Int? = null,
    val initialSubcategoryId: Int? = null,
    val initialShortcutId: Int? = null
)