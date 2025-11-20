package com.jorgelobo.koobe.domain.usecase.category

import com.jorgelobo.koobe.domain.repository.CategoryRepository

class GetAllCategoriesUseCase(
    private val repository: CategoryRepository
) {
    operator fun invoke() = repository.getAllCategories()
}