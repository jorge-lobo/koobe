package com.jorgelobo.koobe.domain.usecase.subcategory

import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import javax.inject.Inject

class GetAllSubcategoriesUseCase @Inject constructor(
    private val repository: SubcategoryRepository
) {
    operator fun invoke() = repository.getAllSubcategories()
}