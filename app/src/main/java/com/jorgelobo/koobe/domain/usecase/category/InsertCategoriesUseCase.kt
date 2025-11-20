package com.jorgelobo.koobe.domain.usecase.category

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.repository.CategoryRepository

class InsertCategoriesUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(list: List<Category>) {
        repository.insertCategories(list)
    }
}