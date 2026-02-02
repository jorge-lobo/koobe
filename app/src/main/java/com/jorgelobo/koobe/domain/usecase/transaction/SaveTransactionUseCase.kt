package com.jorgelobo.koobe.domain.usecase.transaction

import com.jorgelobo.koobe.domain.model.transaction.Transaction
import javax.inject.Inject

/**
 * Saves a transaction by delegating to the appropriate persistence use case.
 *
 * This use case encapsulates the decision of whether a transaction should be created or updated,
 * based on the current editor context.
 *
 * It is intended to be used by presentation layers to avoid duplicating conditional save logic.
 */
class SaveTransactionUseCase @Inject constructor(
    private val insertTransaction: InsertTransactionUseCase,
    private val updateTransaction: UpdateTransactionUseCase
) {

    /**
     * Persists a transaction by either inserting or updating it.
     *
     * @param transaction The transaction to be saved
     * @param isEditorMode Whether the transaction is being edited (true)
     *                     or created for the first time (false)
     */
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