package com.jorgelobo.koobe.data.repository

import com.jorgelobo.koobe.data.local.dao.BudgetDao
import com.jorgelobo.koobe.data.mapper.toDomain
import com.jorgelobo.koobe.data.mapper.toEntity
import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val dao: BudgetDao
) : BudgetRepository {

    override fun getAllBudgets(): Flow<List<Budget>> =
        dao.getAll().map { list ->
            list.map { it.toDomain() }
        }

    override fun getBudgetsByCategory(categoryId: Int): Flow<List<Budget>> =
        dao.getByCategoryId(categoryId).map { list ->
            list.map { it.toDomain() }
        }

    override fun getBudgetsBySubcategory(subcategoryId: Int): Flow<List<Budget>> =
        dao.getBySubcategoryId(subcategoryId).map { list ->
            list.map { it.toDomain() }
        }

    override fun getBudgetsByPeriod(period: PeriodType): Flow<List<Budget>> =
        dao.getByPeriod(period).map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun getBudgetById(budgetId: Int): Budget? =
        dao.getById(budgetId)?.toDomain()

    override suspend fun insertBudget(budget: Budget) {
        dao.insert(budget.toEntity())
    }

    override suspend fun deleteBudget(budget: Budget) {
        dao.delete(budget.toEntity())
    }
}