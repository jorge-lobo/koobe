package com.jorgelobo.koobe.domain.usecase.subcategory

import com.jorgelobo.koobe.domain.repository.SubcategoryRepository

class GetSubcategoriesByCategoryIdUseCase(
    private val repository: SubcategoryRepository
) {
    operator fun invoke(categoryId: Int) = repository.getSubcategoriesByCategoryId(categoryId)
}