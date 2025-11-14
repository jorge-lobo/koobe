package com.jorgelobo.koobe.domain.repository

import com.jorgelobo.koobe.domain.model.category.Subcategory

interface SubcategoryRepository {
    suspend fun getSubcategoryById(subcategoryId: Int): Subcategory?
    suspend fun getSubcategoriesByCategory(categoryId: Int): List<Subcategory>
}