package com.jorgelobo.koobe.domain.usecase.category

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.repository.CategoryRepository

class InsertCategoryUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: Category) {
        repository.insertCategory(category)
    }
}