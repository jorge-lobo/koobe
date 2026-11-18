package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.usecase.budget.GetAllBudgetsUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class HasConflictingBudgetUseCase @Inject constructor(
    private val getAllBudgets: GetAllBudgetsUseCase,
    private val getBudgetPeriod: GetBudgetPeriodUseCase
) {

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