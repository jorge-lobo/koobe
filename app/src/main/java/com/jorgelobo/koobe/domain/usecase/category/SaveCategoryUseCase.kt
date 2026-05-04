package com.jorgelobo.koobe.domain.usecase.category

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.ui.mappers.CategoryNameResolver
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SaveCategoryUseCase @Inject constructor(
    private val insertCategory: InsertCategoryUseCase,
    private val updateCategory: UpdateCategoryUseCase,
    private val categoryRepository: CategoryRepository,
    private val nameResolver: CategoryNameResolver
) {

    suspend operator fun invoke(
        category: Category,
        isEditMode: Boolean
    ) {

        val rawName = category.name.trim()
        val normalizedName = normalizeForComparison(rawName)
        val allCategories = categoryRepository.getAllCategories().first()

        val exists = allCategories.any {
            it.id != category.id && normalizeForComparison(it.name) == normalizedName
        }

        if (rawName.isBlank()) {
            throw CategoryValidationException.EmptyName()
        }

        if (exists) {
            throw CategoryValidationException.DuplicateName()
        }

        if (isEditMode) {
            updateCategory(category)
        } else {
            insertCategory(category)
        }
    }

    private fun normalizeForComparison(name: String): String {
        return nameResolver
            .resolve(name)
            .trim()
            .lowercase()
    }
}