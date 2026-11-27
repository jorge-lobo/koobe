package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.usecase.budget.GetAllBudgetsUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Checks whether a candidate budget conflicts with any existing active budget.
 *
 * A conflict exists if there is already an active budget for the same subcategory and period type:
 * - If the existing budget is recurring, it conflicts with any candidate budget of that subcategory and period type.
 * - If the existing budget is non-recurring, it conflicts only if its calculated period start date matches the candidate's period start date.
 * - Inactive budgets and the candidate budget itself (matching ID during updates) are ignored.
 */
class HasConflictingBudgetUseCase @Inject constructor(
    private val getAllBudgets: GetAllBudgetsUseCase,
    private val getBudgetPeriod: GetBudgetPeriodUseCase
) {

    /**
     * Determines if the given [budget] conflicts with any existing active budget.
     *
     * @param budget The candidate budget to check for conflicts.
     * @param startOfWeek The start of week setting used to calculate period boundaries.
     * @return `true` if a conflicting active budget exists, `false` otherwise.
     */
    suspend operator fun invoke(
        budget: Budget,
        startOfWeek: StartOfWeek
    ) : Boolean {
        val budgets = getAllBudgets().first()

        val candidatePeriod = getBudgetPeriod(
            date = budget.periodStartDate,
            periodType = budget.period,
            startOfWeek = startOfWeek
        )

        return budgets.any { existing ->
            if (existing.id == budget.id) return@any false

            if (existing.subcategoryId != budget.subcategoryId) return@any false

            if (existing.period != budget.period) return@any false

            if (!existing.isActive) return@any false

            if (existing.repeat) return@any true

            val existingPeriod = getBudgetPeriod(
                date = existing.periodStartDate,
                periodType = existing.period,
                startOfWeek = startOfWeek
            )

            existingPeriod.startDate == candidatePeriod.startDate
        }
    }
}