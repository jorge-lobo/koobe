package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.utils.date.DateUtils
import java.util.Date
import javax.inject.Inject

/**
 * Use case responsible for determining whether a given [Budget] has expired.
 *
 * Expiration rules:
 * 1. If [Budget.repeat] is `true`, the budget never expires and this returns `false`.
 * 2. If [Budget.repeat] is `false`, its period is computed from [Budget.periodStartDate] using [GetBudgetPeriodUseCase].
 *    The budget is considered expired if the target date is on or after the period's end date.
 */
class IsBudgetExpiredUseCase @Inject constructor(
    private val getBudgetPeriod: GetBudgetPeriodUseCase
) {

    /**
     * Evaluates whether the given [budget] has expired relative to the specified [date].
     *
     * @param budget The [Budget] instance to evaluate.
     * @param date The reference date to check expiration against (defaults to the current date).
     * @param startOfWeek User preference specifying the first day of the week, used for period calculations.
     * @return `true` if the budget does not repeat and the [date] is on or after its period end date; `false` otherwise.
     */
    operator fun invoke(
        budget: Budget,
        date: Date = DateUtils.currentDate,
        startOfWeek: StartOfWeek
    ): Boolean {
        if (budget.repeat) return false

        val period = getBudgetPeriod(
            date = budget.periodStartDate,
            periodType = budget.period,
            startOfWeek = startOfWeek
        )

        return !date.before(period.endDate)
    }
}