package com.jorgelobo.koobe.data.repository

import com.jorgelobo.koobe.data.local.dao.CategoryDao
import com.jorgelobo.koobe.data.mapper.toDomain
import com.jorgelobo.koobe.data.mapper.toEntity
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> =
        dao.getAll().map { list ->
            list.map { it.toDomain() }
        }

    override fun getCategoriesByTransactionType(type: TransactionType): Flow<List<Category>> =
        dao.getByTransactionType(type).map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun getCategoryById(categoryId: Int): Category? =
        dao.getById(categoryId)?.toDomain()

    override suspend fun insertCategory(category: Category) {
        dao.insert(category.toEntity())
    }

    override suspend fun insertCategories(list: List<Category>) {
        dao.insertAll(list.map { it.toEntity() })
    }

    override suspend fun updateCategory(category: Category) {
        dao.update(category.toEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        dao.delete(category.toEntity())
    }
}