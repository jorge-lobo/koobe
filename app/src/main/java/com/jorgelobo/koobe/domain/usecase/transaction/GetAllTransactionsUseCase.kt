package com.jorgelobo.koobe.domain.usecase.transaction

import com.jorgelobo.koobe.domain.repository.TransactionRepository

class GetAllTransactionsUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke() = repository.getAllTransactions()
}