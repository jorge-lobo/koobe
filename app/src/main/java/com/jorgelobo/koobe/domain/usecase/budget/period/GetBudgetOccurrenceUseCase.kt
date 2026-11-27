package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.budget.BudgetPeriod
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import java.util.Date
import javax.inject.Inject

/**
 * Calculates a specific occurrence period for a recurring budget.
 *
 * Computes the [BudgetPeriod] (start and end date) corresponding to a target date
 * for an active, recurring [Budget]. Ensures that the budget is eligible for occurrence calculation
 * and that both the target date and resulting occurrence fall on or after the budget's start date.
 */
class GetBudgetOccurrenceUseCase @Inject constructor(
    private val getBudgetPeriod: GetBudgetPeriodUseCase
) {

    /**
     * Calculates the [BudgetPeriod] occurrence for the given [budget] containing the specified [date].
     *
     * @param budget The recurring budget for which to calculate the occurrence. Must be active and set to repeat.
     * @param date The target date within the requested occurrence period. Must not be before [Budget.periodStartDate].
     * @param startOfWeek User preference specifying the first day of the week, used when calculating weekly period boundaries.
     * @return The calculated [BudgetPeriod] representing the start and end dates of the occurrence.
     * @throws IllegalArgumentException If [budget] is not recurring, is inactive, or if [date] or the calculated occurrence precedes [Budget.periodStartDate].
     */
    operator fun invoke(
        budget: Budget,
        date: Date,
        startOfWeek: StartOfWeek
    ): BudgetPeriod {
        require(budget.repeat) {
            "Cannot calculate a recurring occurrence for a non-recurring budget."
        }

        require(budget.isActive) {
            "Cannot calculate an occurrence for an inactive budget."
        }

        require(!date.before(budget.periodStartDate)) {
            "The requested date cannot be before the budget start date"
        }

        val occurrence = getBudgetPeriod(
            date = date,
            periodType = budget.period,
            startOfWeek = startOfWeek
        )

        require(!occurrence.startDate.before(budget.periodStartDate)) {
            "The calculated occurrence cannot be before the budget start date."
        }

        return occurrence
    }
}