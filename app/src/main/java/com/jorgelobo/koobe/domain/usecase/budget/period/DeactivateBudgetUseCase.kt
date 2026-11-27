package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.usecase.budget.UpdateBudgetUseCase
import javax.inject.Inject

/**
 * Use case responsible for deactivating a budget.
 *
 * Updates the target budget's active state to `false` and persists the updated budget via [UpdateBudgetUseCase].
 */
class DeactivateBudgetUseCase @Inject constructor(
    private val updateBudget: UpdateBudgetUseCase
) {

    /**
     * Deactivates the given [budget] if it is currently active.
     *
     * If the budget is already inactive, the operation is skipped.
     *
     * @param budget The [Budget] to be deactivated.
     */
    suspend operator fun invoke(budget: Budget) {
        if (!budget.isActive) return

        updateBudget(
            budget.copy(isActive = false)
        )
    }
}