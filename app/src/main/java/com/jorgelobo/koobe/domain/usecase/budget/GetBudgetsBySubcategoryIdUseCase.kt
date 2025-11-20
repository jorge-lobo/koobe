package com.jorgelobo.koobe.domain.usecase.budget

import com.jorgelobo.koobe.domain.repository.BudgetRepository

class GetBudgetsBySubcategoryIdUseCase(
    private val repository: BudgetRepository
) {
    operator fun invoke(subcategoryId: Int) = repository.getBudgetsBySubcategoryId(subcategoryId)
}