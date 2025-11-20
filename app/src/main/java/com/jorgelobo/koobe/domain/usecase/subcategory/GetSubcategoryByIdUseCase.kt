package com.jorgelobo.koobe.domain.usecase.subcategory

import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository

class GetSubcategoryByIdUseCase(
    private val repository: SubcategoryRepository
) {
    suspend operator fun invoke(id: Int): Subcategory? =
        repository.getSubcategoryById(id)
}