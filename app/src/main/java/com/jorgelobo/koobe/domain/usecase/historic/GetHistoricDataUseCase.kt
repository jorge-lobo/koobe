package com.jorgelobo.koobe.domain.usecase.historic

import com.jorgelobo.koobe.domain.model.category.CategoryHistory
import com.jorgelobo.koobe.domain.model.category.SubcategoryHistory
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.repository.TransactionRepository
import com.jorgelobo.koobe.domain.usecase.category.GetCategoriesByTransactionTypeUseCase
import com.jorgelobo.koobe.domain.usecase.subcategory.GetAllSubcategoriesUseCase
import com.jorgelobo.koobe.utils.date.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Date
import javax.inject.Inject

class GetHistoricDataUseCase @Inject constructor(
    private val getCategoriesByType: GetCategoriesByTransactionTypeUseCase,
    private val getAllSubcategories: GetAllSubcategoriesUseCase,
    private val transactionRepositor: TransactionRepository
) {

    operator fun invoke(
        date: Date,
        transactionType: TransactionType,
        periodType: PeriodType
    ): Flow<List<CategoryHistory>> {

        val (startDate, endDate) = DateUtils.getPeriodRange(date, periodType)
        val startMillis = startDate.time
        val endMillis = endDate.time

        return combine(
            getCategoriesByType(transactionType),
            getAllSubcategories(),
            transactionRepositor.getTransactionsByPeriod(
                type = transactionType,
                startDate = startMillis,
                endDate = endMillis
            )
        ) { categories, subcategories, transactions ->

            val transactionsByCategory = transactions.groupBy { it.categoryId }
            val transactionsBySubcategory = transactions.groupBy { it.subcategoryId }

            categories.mapNotNull { category ->

                val categoryTransactions = transactionsByCategory[category.id].orEmpty()
                if (categoryTransactions.isEmpty()) return@mapNotNull null

                val categorySubcategories = subcategories.filter { it.categoryId == category.id }

                val subcategoryHistories = categorySubcategories.mapNotNull { subcategory ->

                    val subcategoryTransactions =
                        transactionsBySubcategory[subcategory.id].orEmpty()
                    if (subcategoryTransactions.isEmpty()) return@mapNotNull null

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
            }
        }
    }
}