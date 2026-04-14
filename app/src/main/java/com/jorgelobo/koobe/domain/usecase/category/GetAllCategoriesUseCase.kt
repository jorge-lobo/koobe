package com.jorgelobo.koobe.domain.usecase.category

import com.jorgelobo.koobe.domain.repository.CategoryRepository
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke() = repository.getAllCategories()
}