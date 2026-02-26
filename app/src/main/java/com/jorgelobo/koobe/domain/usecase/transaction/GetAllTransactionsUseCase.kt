package com.jorgelobo.koobe.domain.usecase.transaction

import com.jorgelobo.koobe.domain.repository.TransactionRepository
import javax.inject.Inject

class GetAllTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke() = repository.getAllTransactions()
}