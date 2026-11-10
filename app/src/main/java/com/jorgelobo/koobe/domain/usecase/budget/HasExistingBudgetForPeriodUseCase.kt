package com.jorgelobo.koobe.domain.usecase.budget

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Use case that checks if a budget already exists for a specific subcategory and period.
 *
 * This is primarily used for validation to prevent duplicate budget entries for the same
 * timeframe within a subcategory.
 *
 * @property getBudgetsBySubcategoryId Use case to retrieve all budgets associated with a subcategory.
 */
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