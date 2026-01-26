package com.jorgelobo.koobe.domain.usecase.transaction

import com.jorgelobo.koobe.domain.model.transaction.Transaction
import javax.inject.Inject

class SaveTransactionUseCase @Inject constructor(
    private val insertTransaction: InsertTransactionUseCase,
    private val updateTransaction: UpdateTransactionUseCase
) {
    suspend operator fun invoke(
        transaction: Transaction,
        isEditorMode: Boolean
    ) {
        if (isEditorMode) {
            updateTransaction(transaction)
        } else {
            insertTransaction(transaction)
        }
    }
}