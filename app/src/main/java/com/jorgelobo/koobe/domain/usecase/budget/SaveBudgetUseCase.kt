package com.jorgelobo.koobe.domain.usecase.budget

import com.jorgelobo.koobe.domain.model.budget.Budget
import javax.inject.Inject

class SaveBudgetUseCase @Inject constructor(
    private val insertBudget: InsertBudgetUseCase,
    private val updateBudget: UpdateBudgetUseCase
) {

    suspend operator fun invoke(
        budget: Budget,
        isEditorMode: Boolean
    ) {
        if (isEditorMode) {
            updateBudget(budget)
        } else {
            insertBudget(budget)
        }
    }
}