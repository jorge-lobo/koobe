package com.jorgelobo.koobe.ui.mappers

import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorConfig
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorUiState

fun TransactionEditorUiState.toTransaction(
    config: TransactionEditorConfig
): Transaction {
    return Transaction(
        id = config.transactionId,
        date = date,
        description = description?.trim().orEmpty(),
        type = initialSnapshot.transactionType,
        categoryId = category.id,
        subcategoryId = subcategory?.id,
        amount = amount,
        paymentMethod = paymentMethodType,
        currency = currencyType
    )
}