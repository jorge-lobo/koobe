package com.jorgelobo.koobe.domain.usecase.historic

import com.jorgelobo.koobe.domain.model.category.CategoryHistory
import com.jorgelobo.koobe.domain.model.category.SubcategoryHistory
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.usecase.category.GetCategoriesByTransactionTypeUseCase
import com.jorgelobo.koobe.domain.usecase.transaction.GetAllTransactionsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetHistoricDataUseCase @Inject constructor(
    private val getCategoriesByType: GetCategoriesByTransactionTypeUseCase,
    private val getAllTransactions: GetAllTransactionsUseCase
) {

    operator fun invoke(type: TransactionType): Flow<List<CategoryHistory>> {

        return combine(
            getCategoriesByType(type),
            getAllTransactions()
        ) { categories, transactions ->
            categories.map { category ->
                val categoryTransactions = transactions.filter { it.categoryId == category.id }

                val subcategoryHistories = category.subcategories.map { subcategory ->
                    val subcategoryTransactions =
                        categoryTransactions.filter { it.subcategoryId == subcategory.id }

                    SubcategoryHistory(
                        subcategory = subcategory,
                        transactionCount = subcategoryTransactions.size,
                        totalAmount = subcategoryTransactions.sumOf { it.amount },
                        transactions = subcategoryTransactions
                    )
                }

                CategoryHistory(
                    category = category,
                    transactionCount = categoryTransactions.size,
                    totalAmount = categoryTransactions.sumOf { it.amount },
                    subcategories = subcategoryHistories
                )
            }.filter { it.transactionCount > 0 }
        }
    }
}