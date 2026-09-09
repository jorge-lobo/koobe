package com.jorgelobo.koobe.domain.usecase.budget

import com.jorgelobo.koobe.domain.repository.BudgetRepository
import javax.inject.Inject

class GetAllBudgetsUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    operator fun invoke() = repository.getAllBudgets()
}