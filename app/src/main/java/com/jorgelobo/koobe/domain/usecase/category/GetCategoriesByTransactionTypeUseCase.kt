package com.jorgelobo.koobe.domain.usecase.category

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class GetCategoriesByTransactionTypeUseCase(
    private val repository: CategoryRepository
) {
    operator fun invoke(type: TransactionType): Flow<List<Category>> =
        repository.getCategoriesByTransactionType(type)
}