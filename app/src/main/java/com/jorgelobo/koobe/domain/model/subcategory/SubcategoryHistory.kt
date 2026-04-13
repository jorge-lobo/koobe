package com.jorgelobo.koobe.domain.model.subcategory

import com.jorgelobo.koobe.domain.model.transaction.Transaction

data class SubcategoryHistory(
    val subcategory: Subcategory,
    val transactionCount: Int,
    val totalAmount: Double,
    val transactions: List<Transaction>
)