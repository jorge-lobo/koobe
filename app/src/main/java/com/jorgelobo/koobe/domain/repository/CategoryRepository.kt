package com.jorgelobo.koobe.domain.repository

import com.jorgelobo.koobe.data.local.entity.CategoryEntity
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    fun getCategoriesByTransactionType(type: TransactionType): Flow<List<Category>>
    suspend fun getCategoryById(categoryId: Int): Category?
    suspend fun insertCategory(category: Category)
    suspend fun insertCategories(list: List<Category>)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    suspend fun insertCategoryEntities(list: List<CategoryEntity>)
    fun getCategoryByIdFlow(id: Int): Flow<Category?>
    fun getCategoriesWithSubcategories(type: TransactionType): Flow<List<Category>>
}