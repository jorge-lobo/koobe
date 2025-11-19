package com.jorgelobo.koobe.domain.usecase

import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository

class GetSubcategoryByIdUseCase(
    private val subcategoryRepository: SubcategoryRepository
) {
    suspend operator fun invoke(subcategoryId: Int): Subcategory? =
        subcategoryRepository.getSubcategoryById(subcategoryId)
}