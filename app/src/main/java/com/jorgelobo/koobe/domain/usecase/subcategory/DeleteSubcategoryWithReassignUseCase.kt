package com.jorgelobo.koobe.domain.usecase.subcategory

import androidx.room.withTransaction
import com.jorgelobo.koobe.data.local.KoobeDatabase
import com.jorgelobo.koobe.domain.model.category.PlaceholderCategories
import com.jorgelobo.koobe.domain.model.category.PlaceholderSubcategories
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
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

            val category = categoryRepository.getCategoryById(subcategory.categoryId)
                ?: error("Category not found")

            val (newCategoryId, newSubcategoryId) = when (category.type) {
                TransactionType.EXPENSE -> {
                    PlaceholderCategories.EXPENSE_ID to PlaceholderSubcategories.EXPENSE_ID
                }

                TransactionType.INCOME -> {
                    PlaceholderCategories.INCOME_ID to PlaceholderSubcategories.INCOME_ID
                }
            }

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