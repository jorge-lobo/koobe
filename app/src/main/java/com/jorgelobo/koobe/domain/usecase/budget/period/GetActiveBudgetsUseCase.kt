package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.usecase.budget.GetAllBudgetsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case responsible for retrieving all active budgets.
 *
 * It observes all budgets via [GetAllBudgetsUseCase] and filters for those that are active,
 * evaluating budget active status using [IsBudgetActiveUseCase] and the user's preferred [StartOfWeek].
 */
class GetActiveBudgetsUseCase @Inject constructor(
    private val getAllBudgets: GetAllBudgetsUseCase,
    private val isBudgetActive: IsBudgetActiveUseCase
) {

    /**
     * Retrieves a reactive stream of budgets that are currently active.
     *
     * @param startOfWeek User preference specifying the first day of the week, used to determine weekly period boundaries.
     * @return A [Flow] emitting a list of active [Budget]s whenever budget data changes.
     */
    operator fun invoke(
        startOfWeek: StartOfWeek
    ): Flow<List<Budget>> = getAllBudgets().map { budgets ->
        budgets.filter { budget ->
            isBudgetActive(
                budget = budget,
                startOfWeek = startOfWeek
            )
        }
    }
}