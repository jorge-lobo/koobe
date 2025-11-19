package com.jorgelobo.koobe.data.repository

import com.jorgelobo.koobe.data.local.dao.SubcategoryDao
import com.jorgelobo.koobe.data.mapper.toDomain
import com.jorgelobo.koobe.data.mapper.toEntity
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SubcategoryRepositoryImpl @Inject constructor(
    private val dao: SubcategoryDao
) : SubcategoryRepository{

    override fun getAllSubcategories(): Flow<List<Subcategory>> =
        dao.getAll().map { list ->
            list.map { it.toDomain() }
        }

    override fun getSubcategoriesByCategory(categoryId: Int): Flow<List<Subcategory>> =
        dao.getByCategoryId(categoryId).map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun getSubcategoryById(subcategoryId: Int): Subcategory? =
        dao.getById(subcategoryId)?.toDomain()


    override suspend fun insertSubcategory(subcategory: Subcategory) {
        dao.insert(subcategory.toEntity())
    }

    override suspend fun insertSubcategories(list: List<Subcategory>) {
        dao.insertAll(list.map { it.toEntity() })
    }

    override suspend fun updateSubcategory(subcategory: Subcategory) {
        dao.update(subcategory.toEntity())
    }

    override suspend fun deleteSubcategory(subcategory: Subcategory) {
        dao.delete(subcategory.toEntity())
    }
}