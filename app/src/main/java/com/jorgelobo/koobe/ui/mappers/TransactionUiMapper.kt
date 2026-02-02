package com.jorgelobo.koobe.ui.mappers

import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorConfig
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorUiState

/**
 * Converts the current [TransactionEditorUiState] into a [Transaction] domain model.
 *
 * @param config The editor configuration containing IDs and mode information.
 * @param resolvedDescription The final transaction description, after resolution (e.g., from user
 * input or auto-filled suggestion).
 *
 * This function is used when saving or updating a transaction, encapsulating the mapping from
 * UI state to the domain entity.
 */
fun TransactionEditorUiState.toTransaction(
    config: TransactionEditorConfig,
    resolvedDescription: String
): Transaction {
    return Transaction(
        id = config.transactionId,
        date = date,
        description = resolvedDescription.trim(),
        type = initialSnapshot.transactionType,
        categoryId = category.id,
        subcategoryId = subcategory?.id,
        amount = amount,
        paymentMethod = paymentMethodType,
        currency = currencyType
    )
}