package com.jorgelobo.koobe.domain.usecase.subcategory

import com.jorgelobo.koobe.domain.model.category.Subcategory
import javax.inject.Inject

class SaveSubcategoryCaseUse @Inject constructor(
    private val insertSubcategory: InsertSubcategoryUseCase,
    private val updateSubcategory: UpdateSubcategoryUseCase
) {

    suspend operator fun invoke(
        subcategory: Subcategory,
        isEditMode: Boolean
    ) {
        if (isEditMode) {
            updateSubcategory(subcategory)
        } else {
            insertSubcategory(subcategory)
        }
    }
}