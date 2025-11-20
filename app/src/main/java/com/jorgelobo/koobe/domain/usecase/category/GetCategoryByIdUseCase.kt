package com.jorgelobo.koobe.domain.usecase.category

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.repository.CategoryRepository

class GetCategoryByIdUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(id: Int): Category? =
        repository.getCategoryById(id)
}