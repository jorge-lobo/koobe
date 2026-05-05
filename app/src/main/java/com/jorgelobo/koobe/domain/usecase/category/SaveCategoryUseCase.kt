package com.jorgelobo.koobe.domain.usecase.category

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.validation.NameValidationUseCase
import com.jorgelobo.koobe.ui.mappers.CategoryNameResolver
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SaveCategoryUseCase @Inject constructor(
    private val insertCategory: InsertCategoryUseCase,
    private val updateCategory: UpdateCategoryUseCase,
    private val nameValidation: NameValidationUseCase,
    private val categoryRepository: CategoryRepository,
    private val nameResolver: CategoryNameResolver
) {

    suspend operator fun invoke(
        category: Category,
        isEditMode: Boolean
    ) {

        val allCategories = categoryRepository.getAllCategories().first()

        nameValidation.validate(
            name = category.name,
            currentId = category.id,
            existingItems = allCategories,
            extractId = { it.id },
            extractName = { it.name },
            normalize = {input -> nameResolver.resolve(input).trim().lowercase() }
        )

        if (isEditMode) {
            updateCategory(category)
        } else {
            insertCategory(category)
        }
    }
}