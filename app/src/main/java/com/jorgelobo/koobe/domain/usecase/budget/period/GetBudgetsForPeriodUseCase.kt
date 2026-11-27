package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.usecase.budget.GetAllBudgetsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

/**
 * Retrieves all active budgets for a specific date and week configuration.
 *
 * Filters all available budgets, retaining only those that are active on the target date
 * according to the specified start of week setting.
 */
class GetBudgetsForPeriodUseCase @Inject constructor(
    private val getAllBudgets: GetAllBudgetsUseCase,
    private val isBudgetActive: IsBudgetActiveUseCase
) {

    /**
     * Executes the use case to obtain a stream of active budgets for the given date.
     *
     * @param date The reference date to evaluate budget activity against.
     * @param startOfWeek The user preference specifying the first day of the week.
     * @return A [Flow] emitting a list of active [Budget] objects for the specified date.
     */
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