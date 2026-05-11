package com.jorgelobo.koobe.domain.usecase.category

import androidx.room.withTransaction
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.data.local.KoobeDatabase
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.transaction.toPlaceholderIds
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.TransactionRepository
import javax.inject.Inject

/**
 * Deletes a category and reassigns its transactions to the placeholder category for the
 * corresponding [TransactionType].
 *
 * Runs inside a database transaction.
 *
 * @throws IllegalStateException if the category no longer exists.
 * @throws IllegalArgumentException if the category is the fallback and cannot be deleted.
 */
class DeleteCategoryWithReassignUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
    private val deleteCategory: DeleteCategoryUseCase,
    private val database: KoobeDatabase
) {

    suspend operator fun invoke(category: Category) {

        database.withTransaction {

            val currentCategory = categoryRepository.getCategoryById(category.id)
                ?: error("Category not found")

            val (newCategoryId, newSubcategoryId) = currentCategory.type.toPlaceholderIds()

            require(currentCategory.id != newCategoryId) {
                "Cannot delete fallback category"
            }

            transactionRepository.reassignCategoryAndSubcategory(
                currentCategory.id,
                newCategoryId,
                newSubcategoryId
            )

            deleteCategory(currentCategory)
        }
    }
}