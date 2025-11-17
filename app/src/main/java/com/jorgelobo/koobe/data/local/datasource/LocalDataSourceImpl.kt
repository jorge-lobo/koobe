package com.jorgelobo.koobe.data.local.datasource

import com.jorgelobo.koobe.data.local.dao.*
import com.jorgelobo.koobe.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao,
    private val shortcutDao: ShortcutDao,
    private val subcategoryDao: SubcategoryDao,
    private val transactionDao: TransactionDao
) : LocalDataSource {

    // Budgets
    override fun getBudgets(): Flow<List<BudgetEntity>> = budgetDao.getAll()

    override suspend fun insertBudget(budget: BudgetEntity) {
        budgetDao.insert(budget)
    }

    // Categories
    override fun getCategories(): Flow<List<CategoryEntity>> = categoryDao.getAll()

    override suspend fun insertCategory(category: CategoryEntity) {
        categoryDao.insert(category)
    }

    override suspend fun insertCategories(list: List<CategoryEntity>) {
        categoryDao.insertAll(list)
    }

    override suspend fun deleteCategory(category: CategoryEntity) {
        categoryDao.delete(category)
    }

    // Shortcuts
    override fun getShortcuts(): Flow<List<ShortcutEntity>> = shortcutDao.getAll()

    override suspend fun insertShortcut(shortcut: ShortcutEntity) {
        shortcutDao.insert(shortcut)
    }

    // Subcategories
    override fun getSubcategories(): Flow<List<SubcategoryEntity>> = subcategoryDao.getAll()

    override suspend fun getSubcategoriesByCategoryId(categoryId: Int): List<SubcategoryEntity> =
        subcategoryDao.getByCategoryId(categoryId)

    // Transactions
    override fun getTransactions(): Flow<List<TransactionEntity>> = transactionDao.getAll()

    override suspend fun insertTransaction(transaction: TransactionEntity) =
        transactionDao.insert(transaction)
}