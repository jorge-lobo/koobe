package com.jorgelobo.koobe.domain.usecase.subcategory

import com.jorgelobo.koobe.domain.repository.SubcategoryRepository

class GetAllSubcategoriesUseCase(
    private val repository: SubcategoryRepository
) {
    operator fun invoke() = repository.getAllSubcategories()
}