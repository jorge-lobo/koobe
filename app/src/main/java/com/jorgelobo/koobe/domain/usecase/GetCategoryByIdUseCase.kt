package com.jorgelobo.koobe.domain.usecase

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.repository.CategoryRepository

class GetCategoryByIdUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categoryId: Int): Category? {
        return categoryRepository.getCategoryById(categoryId)
    }
}