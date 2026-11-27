package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.usecase.budget.GetAllBudgetsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case responsible for retrieving all expired budgets.
 *
 * It observes all budgets via [GetAllBudgetsUseCase] and filters out those that are not expired,
 * evaluating budget expiration using [IsBudgetExpiredUseCase] and the user's preferred [StartOfWeek].
 */
class GetExpiredBudgetsUseCase @Inject constructor(
    private val getAllBudgets: GetAllBudgetsUseCase,
    private val isBudgetExpired: IsBudgetExpiredUseCase,
) {

    /**
     * Retrieves a reactive stream of budgets that have passed their expiration date and do not repeat.
     *
     * @param startOfWeek User preference specifying the first day of the week, used to determine weekly period boundaries.
     * @return A [Flow] emitting a list of expired [Budget]s whenever budget data changes.
     */
    operator fun invoke(
        startOfWeek: StartOfWeek,
    ): Flow<List<Budget>> = getAllBudgets().map { budgets ->
        budgets.filter { budget ->
            isBudgetExpired(
                budget = budget,
                startOfWeek = startOfWeek,
            )
        }
    }
}