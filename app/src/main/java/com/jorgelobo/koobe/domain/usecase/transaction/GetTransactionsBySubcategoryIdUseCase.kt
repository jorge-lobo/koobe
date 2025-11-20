package com.jorgelobo.koobe.domain.usecase.transaction

import com.jorgelobo.koobe.domain.repository.TransactionRepository

class GetTransactionsBySubcategoryIdUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(subcategoryId: Int) = repository.getTransactionsBySubcategoryId(subcategoryId)
}