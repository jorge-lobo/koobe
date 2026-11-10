package com.jorgelobo.koobe.domain.usecase.budget

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBudgetsBySubcategoryIdUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    operator fun invoke(subcategoryId: Int): Flow<List<Budget>> {
        return repository.getBudgetsBySubcategoryId(subcategoryId)
    }
}