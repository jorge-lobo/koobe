package com.jorgelobo.koobe.domain.usecase.transaction

import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.domain.repository.TransactionRepository
import javax.inject.Inject

/**
 * Use case responsible for inserting a transaction into the system.
 *
 * This class acts as a domain-level entry point for transaction creation, abstracting the
 * underlying data source implementation.
 */
class InsertTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.insertTransaction(transaction)
    }
}