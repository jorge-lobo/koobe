package com.jorgelobo.koobe.domain.usecase.subcategory

import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository

class InsertSubcategoriesUseCase(
    private val repository: SubcategoryRepository
) {
    suspend operator fun invoke(list: List<Subcategory>) {
        repository.insertSubcategories(list)
    }
}