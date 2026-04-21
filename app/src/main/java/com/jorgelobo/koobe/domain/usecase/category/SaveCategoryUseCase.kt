package com.jorgelobo.koobe.domain.usecase.category

import com.jorgelobo.koobe.domain.model.category.Category
import javax.inject.Inject

class SaveCategoryUseCase @Inject constructor(
    private val insertCategory: InsertCategoryUseCase,
    private val updateCategory: UpdateCategoryUseCase
) {

    suspend operator fun invoke(
        category: Category,
        isEditMode: Boolean
    ) {
        if (isEditMode) {
            updateCategory(category)
        } else {
            insertCategory(category)
        }
    }
}