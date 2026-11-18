package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.usecase.budget.UpdateBudgetUseCase
import javax.inject.Inject

class DeactivateBudgetUseCase @Inject constructor(
    private val updateBudget: UpdateBudgetUseCase
) {

    suspend operator fun invoke(budget: Budget) {
        if (!budget.isActive) return

        updateBudget(
            budget.copy(isActive = false)
        )
    }
}