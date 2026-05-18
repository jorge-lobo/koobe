package com.jorgelobo.koobe.domain.usecase.subcategory

import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.domain.validation.NameValidationUseCase
import com.jorgelobo.koobe.ui.mappers.SubcategoryNameResolver
import com.jorgelobo.koobe.domain.validation.NameValidationException
import kotlinx.coroutines.flow.first
import javax.inject.Inject

// Validates and persists a subcategory, inserting or updating based on the edit mode.
class SaveSubcategoryCaseUse @Inject constructor(
    private val insertSubcategory: InsertSubcategoryUseCase,
    private val updateSubcategory: UpdateSubcategoryUseCase,
    private val nameValidation: NameValidationUseCase,
    private val subcategoryRepository: SubcategoryRepository,
    private val nameResolver: SubcategoryNameResolver
) {

    /**
     * Persists a subcategory after validating its name for structural rules and uniqueness.
     *
     * @param isEditMode If true, updates the existing subcategory; otherwise inserts a new one.
     * @throws NameValidationException if the name is invalid or duplicated.
     */
    suspend operator fun invoke(
        subcategory: Subcategory,
        isEditMode: Boolean
    ) {

        val allSubcategories = subcategoryRepository.getAllSubcategories().first()

        nameValidation.validate(
            name = subcategory.name,
            currentId = subcategory.id,
            existingItems = allSubcategories,
            extractId = { it.id },
            extractName = { it.name },
            normalize = { input -> nameResolver.resolve(input).trim().lowercase() }
        )

        if (isEditMode) {
            updateSubcategory(subcategory)
        } else {
            insertSubcategory(subcategory)
        }
    }
}