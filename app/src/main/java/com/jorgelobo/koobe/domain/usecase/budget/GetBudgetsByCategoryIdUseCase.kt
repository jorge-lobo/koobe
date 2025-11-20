package com.jorgelobo.koobe.domain.usecase.budget

import com.jorgelobo.koobe.domain.repository.BudgetRepository

class GetBudgetsByCategoryIdUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke(categoryId: Int) = repository.getBudgetsByCategoryId(categoryId)
}