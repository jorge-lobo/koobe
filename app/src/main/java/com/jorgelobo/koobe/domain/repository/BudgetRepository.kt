package com.jorgelobo.koobe.domain.repository

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getAllBudgets(): Flow<List<Budget>>
    fun getBudgetsByCategory(categoryId: Int): Flow<List<Budget>>
    fun getBudgetsBySubcategory(subcategoryId: Int): Flow<List<Budget>>
    fun getBudgetsByPeriod(period: PeriodType): Flow<List<Budget>>
    suspend fun getBudgetById(budgetId: Int): Budget?
    suspend fun insertBudget(budget: Budget)
    suspend fun deleteBudget(budget: Budget)
}