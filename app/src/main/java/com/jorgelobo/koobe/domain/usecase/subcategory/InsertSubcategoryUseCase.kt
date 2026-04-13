package com.jorgelobo.koobe.domain.usecase.subcategory

import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import javax.inject.Inject

class InsertSubcategoryUseCase @Inject constructor(
    private val subcategoryRepository: SubcategoryRepository,
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(subcategory: Subcategory) {
        val category = categoryRepository.getCategoryById(subcategory.categoryId)

        requireNotNull(category) {
            "Category ${subcategory.categoryId} does not exist"
        }

        subcategoryRepository.insertSubcategory(subcategory)
    }
}