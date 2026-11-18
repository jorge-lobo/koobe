package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.budget.BudgetPeriod
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import java.util.Date
import javax.inject.Inject

class GetBudgetOccurrenceUseCase @Inject constructor(
    private val getBudgetPeriod: GetBudgetPeriodUseCase
) {

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