package com.jorgelobo.koobe.domain.usecase.subcategory

import androidx.room.withTransaction
import com.jorgelobo.koobe.data.local.KoobeDatabase
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.model.transaction.toPlaceholderIds
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.TransactionRepository
import javax.inject.Inject

/**
 * Deletes a subcategory while reassigning all related transactions
 * to a fallback system subcategory based on transaction type.
 *
 * This operation is executed within a database transaction to ensure
 * consistency across category reassignment, transaction updates, and deletion.
 *
 * Fallback subcategories are used to prevent orphaned transactions.
 */
class DeleteSubcategoryWithReassignUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
    private val deleteSubcategory: DeleteSubcategoryUseCase,
    private val database: KoobeDatabase
) {

    suspend operator fun invoke(subcategory: Subcategory) {

        database.withTransaction {

            val currentCategory = categoryRepository.getCategoryById(subcategory.categoryId)
                ?: error("Category not found")

            val (newCategoryId, newSubcategoryId) = currentCategory.type.toPlaceholderIds()

            require(subcategory.id != newSubcategoryId) {
                "Cannot delete fallback subcategory"
            }

            transactionRepository.reassignSubcategory(
                oldSubcategoryId = subcategory.id,
                newSubcategoryId = newSubcategoryId,
                newCategoryId = newCategoryId
            )

            deleteSubcategory(subcategory)
        }
    }
}