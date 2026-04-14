package com.jorgelobo.koobe.domain.usecase.category

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: Category) {
        repository.updateCategory(category)
    }
}