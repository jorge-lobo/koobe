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

/**
 * Use case to retrieve the total income and expense amounts for a specific period.
 *
 * This class calculates the start and end timestamps based on a provided date and period type,
 * fetches the corresponding transactions from the repository, and aggregates the totals
 * categorized by transaction type.
 *
 * @property repository The repository used to access transaction data.
 */
class GetPeriodTotalsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {

    /**
     * Retrieves the total income and expenses for a specific period.
     *
     * This function calculates the date range based on the provided [date] and [periodType],
     * fetches all transactions within that range, and aggregates them into a [PeriodTotals] object.
     *
     * @param date The reference date used to determine the start and end of the period.
     * @param periodType The type of period (e.g., DAY, WEEK, MONTH, YEAR) to calculate totals for.
     * @return A [Flow] emitting the [PeriodTotals] containing the sum of income and expenses.
     */
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