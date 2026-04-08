package com.jorgelobo.koobe.domain.usecase.subcategory

import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import javax.inject.Inject

class DeleteSubcategoryUseCase @Inject constructor(
    private val repository: SubcategoryRepository
) {
    suspend operator fun invoke(subcategory: Subcategory) {
        repository.deleteSubcategory(subcategory)
    }
}