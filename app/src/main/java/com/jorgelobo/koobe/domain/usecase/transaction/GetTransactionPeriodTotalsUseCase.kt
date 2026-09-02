package com.jorgelobo.koobe.domain.usecase.transaction

import com.jorgelobo.koobe.domain.model.balance.PeriodTotals
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class GetTransactionPeriodTotalsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    operator fun invoke(): Flow<PeriodTotals> {
        return transactionRepository
            .getAllTransactions()
            .map(::calculateTotals)
    }

    operator fun invoke(
        startDate: Date,
        endDate: Date
    ): Flow<PeriodTotals> {
        return transactionRepository.getTransactionsByPeriod(
            startDate = startDate.time,
            endDate = endDate.time
        )
            .map(::calculateTotals)
    }

    private fun calculateTotals(transactions: List<Transaction>): PeriodTotals {
        return PeriodTotals(
            income = transactions
                .filter { it.type == TransactionType.INCOME }
                .sumOf { it.amount },
            expenses = transactions
                .filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }
        )
    }
}