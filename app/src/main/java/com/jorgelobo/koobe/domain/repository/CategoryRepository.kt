package com.jorgelobo.koobe.domain.repository

import com.jorgelobo.koobe.domain.model.category.Category

interface CategoryRepository {
    suspend fun getCategoryById(categoryId: Int): Category?
    suspend fun getAllCategories(): List<Category>
}