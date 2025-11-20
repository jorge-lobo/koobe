package com.jorgelobo.koobe.domain.usecase.transaction

import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.domain.repository.TransactionRepository

class GetTransactionByIdUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(id: Int): Transaction? =
        repository.getTransactionById(id)
}