package com.jorgelobo.koobe.domain.usecase.budget

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class HasExistingBudgetForPeriodUseCase @Inject constructor(
    private val getBudgetsBySubcategoryId: GetBudgetsBySubcategoryIdUseCase
) {

    suspend operator fun invoke(
        subcategoryId: Int,
        period: PeriodType,
        budgetId: Int? = null
    ): Boolean {
        val budgets = getBudgetsBySubcategoryId(subcategoryId).first()

        return budgets.any { existingBudget ->
            existingBudget.id != budgetId && existingBudget.period == period
        }
    }
}