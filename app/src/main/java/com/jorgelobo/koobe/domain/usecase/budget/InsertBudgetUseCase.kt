package com.jorgelobo.koobe.domain.usecase.budget

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.repository.BudgetRepository

class InsertBudgetUseCase(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(budget: Budget) {
        repository.insertBudget(budget)
    }
}