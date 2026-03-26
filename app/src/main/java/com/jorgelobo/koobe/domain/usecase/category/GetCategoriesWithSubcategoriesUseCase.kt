package com.jorgelobo.koobe.domain.usecase.category

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesWithSubcategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {

    operator fun invoke(
        transactionType: TransactionType
    ): Flow<List<Category>> {
        return repository.getCategoriesWithSubcategories(transactionType)
    }
}