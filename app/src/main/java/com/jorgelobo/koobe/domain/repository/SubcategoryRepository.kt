package com.jorgelobo.koobe.domain.repository

import com.jorgelobo.koobe.domain.model.category.Subcategory
import kotlinx.coroutines.flow.Flow

interface SubcategoryRepository {
    fun getAllSubcategories(): Flow<List<Subcategory>>
    fun getSubcategoriesByCategory(categoryId: Int): Flow<List<Subcategory>>
    suspend fun getSubcategoryById(subcategoryId: Int): Subcategory?
    suspend fun insertSubcategory(subcategory: Subcategory)
    suspend fun insertSubcategories(list: List<Subcategory>)
    suspend fun updateSubcategory(subcategory: Subcategory)
    suspend fun deleteSubcategory(subcategory: Subcategory)
}