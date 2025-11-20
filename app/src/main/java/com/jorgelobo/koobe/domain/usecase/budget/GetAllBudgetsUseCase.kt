package com.jorgelobo.koobe.domain.usecase.budget

import com.jorgelobo.koobe.domain.repository.BudgetRepository

class GetAllBudgetsUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke() = repository.getAllBudgets()
}