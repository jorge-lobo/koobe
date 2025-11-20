package com.jorgelobo.koobe.domain.usecase.budget

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow

class GetBudgetsByPeriodUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke(period: PeriodType): Flow<List<Budget>> =
        repository.getBudgetsByPeriod(period)
}