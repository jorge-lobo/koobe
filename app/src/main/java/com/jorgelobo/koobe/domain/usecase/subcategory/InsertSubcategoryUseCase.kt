package com.jorgelobo.koobe.domain.usecase.subcategory

import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository

class InsertSubcategoryUseCase(
    private val repository: SubcategoryRepository
) {
    suspend operator fun invoke(subcategory: Subcategory) {
        repository.insertSubcategory(subcategory)
    }
}