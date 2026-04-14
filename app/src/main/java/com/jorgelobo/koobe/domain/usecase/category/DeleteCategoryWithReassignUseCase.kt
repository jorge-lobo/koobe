package com.jorgelobo.koobe.domain.usecase.category

import androidx.room.withTransaction
import com.jorgelobo.koobe.data.local.KoobeDatabase
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.transaction.toPlaceholderIds
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.TransactionRepository
import javax.inject.Inject

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