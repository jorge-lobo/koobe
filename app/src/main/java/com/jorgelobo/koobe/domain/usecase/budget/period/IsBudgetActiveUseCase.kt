package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.utils.date.DateUtils
import java.util.Date
import javax.inject.Inject

/**
 * Use case responsible for determining whether a given [Budget] is currently active.
 *
 * A budget is considered active for a specified reference date if:
 * 1. Its [Budget.isActive] property is `true`.
 * 2. The target date is on or after the budget's [Budget.periodStartDate].
 * 3. The budget has not expired according to [IsBudgetExpiredUseCase].
 */
class IsBudgetActiveUseCase @Inject constructor(
    private val isBudgetExpired: IsBudgetExpiredUseCase
) {

    /**
     * Evaluates whether the [budget] is active on the given [date].
     *
     * @param budget The [Budget] instance to evaluate.
     * @param date The reference date to check activity against (defaults to the current date).
     * @param startOfWeek User preference specifying the first day of the week, used for period calculations.
     * @return `true` if the budget is active on the specified date; `false` otherwise.
     */
    operator fun invoke(
        budget: Budget,
        date: Date = DateUtils.currentDate,
        startOfWeek: StartOfWeek
    ): Boolean {
        if (!budget.isActive) return false

        if (date.before(budget.periodStartDate)) return false

        return !isBudgetExpired(
            budget = budget,
            date = date,
            startOfWeek = startOfWeek
        )
    }
}