package com.jorgelobo.koobe.domain.usecase.subcategory

import androidx.room.withTransaction
import com.jorgelobo.koobe.data.local.KoobeDatabase
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteSubcategoryWithReassignUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
    private val deleteSubcategory: DeleteSubcategoryUseCase,
    private val database: KoobeDatabase
) {

    companion object {
        const val EXPENSE_CATEGORY_ID = 16
        const val EXPENSE_SUBCATEGORY_ID = 79

        const val INCOME_CATEGORY_ID = 22
        const val INCOME_SUBCATEGORY_ID = 99
    }

    suspend operator fun invoke(subcategory: Subcategory) {

        database.withTransaction {

            // Get the category associated with the subcategory
            val category = categoryRepository.getCategoryById(subcategory.categoryId)
                ?: error("Category not found")

            // Reassign the subcategory to the fallback category
            val (newCategoryId, newSubcategoryId) = when (category.type) {
                TransactionType.EXPENSE -> {
                    EXPENSE_CATEGORY_ID to EXPENSE_SUBCATEGORY_ID
                }

                TransactionType.INCOME -> {
                    INCOME_CATEGORY_ID to INCOME_SUBCATEGORY_ID
                }
            }

            // Protect fallback subcategory
            require(subcategory.id != newSubcategoryId) {
                "Cannot delete fallback subcategory"
            }

            // Reassign all transactions associated with the subcategory
            transactionRepository.reassignSubcategory(
                oldSubcategoryId = subcategory.id,
                newSubcategoryId = newSubcategoryId,
                newCategoryId = newCategoryId
            )

            // Delete the subcategory
            deleteSubcategory(subcategory)
        }
    }
}