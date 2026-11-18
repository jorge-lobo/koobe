package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.usecase.budget.GetAllBudgetsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class GetBudgetsForPeriodUseCase @Inject constructor(
    private val getAllBudgets: GetAllBudgetsUseCase,
    private val isBudgetActive: IsBudgetActiveUseCase
) {

    operator fun invoke(
        date: Date,
        startOfWeek: StartOfWeek
    ): Flow<List<Budget>> = getAllBudgets().map { budgets ->
        budgets.filter { budget ->
            isBudgetActive(
                budget = budget,
                date = date,
                startOfWeek = startOfWeek
            )
        }
    }
}