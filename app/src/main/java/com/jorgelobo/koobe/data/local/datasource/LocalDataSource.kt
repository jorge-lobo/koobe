package com.jorgelobo.koobe.data.local.datasource

import com.jorgelobo.koobe.data.local.entity.*
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    // Budgets
    fun getBudgets(): Flow<List<BudgetEntity>>
    suspend fun insertBudget(budget: BudgetEntity)

    // Categories
    fun getCategories(): Flow<List<CategoryEntity>>
    suspend fun insertCategory(category: CategoryEntity)
    suspend fun insertCategories(list: List<CategoryEntity>)
    suspend fun deleteCategory(category: CategoryEntity)

    // Shortcuts
    fun getShortcuts(): Flow<List<ShortcutEntity>>
    suspend fun insertShortcut(shortcut: ShortcutEntity)

    // Subcategories
    fun getSubcategories(): Flow<List<SubcategoryEntity>>
    suspend fun getSubcategoriesByCategoryId(categoryId: Int): List<SubcategoryEntity>

    // Transactions
    fun getTransactions(): Flow<List<TransactionEntity>>
    suspend fun insertTransaction(transaction: TransactionEntity)
}