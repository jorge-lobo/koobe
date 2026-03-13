package com.jorgelobo.koobe.domain.usecase.historic

import com.jorgelobo.koobe.domain.model.balance.PeriodTotals
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.repository.TransactionRepository
import com.jorgelobo.koobe.utils.date.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class GetPeriodTotalsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {

    operator fun invoke(
        date: Date,
        periodType: PeriodType
    ): Flow<PeriodTotals> {

        val (startDate, endDate) = DateUtils.getPeriodRange(date, periodType)

        val startMillis = startDate.time
        val endMillis = endDate.time

        return repository
            .getTransactionsByPeriod(startMillis, endMillis)
            .map { transactions ->

                val income = transactions
                    .filter { it.type == TransactionType.INCOME }
                    .sumOf { it.amount }

                val expenses = transactions
                    .filter { it.type == TransactionType.EXPENSE }
                    .sumOf { it.amount }

                PeriodTotals(income, expenses)
            }
    }
}