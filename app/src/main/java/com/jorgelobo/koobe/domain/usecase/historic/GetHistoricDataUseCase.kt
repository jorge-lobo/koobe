package com.jorgelobo.koobe.domain.usecase.historic

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.CategoryHistory
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.category.SubcategoryHistory
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Transaction
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
    private val transactionRepository: TransactionRepository
) {

    operator fun invoke(
        date: Date,
        transactionType: TransactionType,
        periodType: PeriodType
    ): Flow<List<CategoryHistory>> {

        val (startDate, endDate) = DateUtils.getPeriodRange(date, periodType)

        return combine(
            getCategoriesByType(transactionType),
            getAllSubcategories(),
            transactionRepository.getTransactionsByPeriod(
                type = transactionType,
                startDate = startDate.time,
                endDate = endDate.time
            )
        ) { categories, subcategories, transactions ->

            buildCategoryHistory(
                categories,
                subcategories,
                transactions
            )
        }
    }

    private fun buildCategoryHistory(
        categories: List<Category>,
        subcategories: List<Subcategory>,
        transactions: List<Transaction>
    ): List<CategoryHistory> {

        val transactionsByCategory = transactions.groupBy { it.categoryId }
        val transactionsBySubcategory = transactions.groupBy { it.subcategoryId }
        val subcategoriesByCategory = subcategories.groupBy { it.categoryId }

        return categories.mapNotNull { category ->
            val categoryTransactions = transactionsByCategory[category.id].orEmpty()

            if (categoryTransactions.isEmpty()) return@mapNotNull null

            val categorySubcategories = subcategoriesByCategory[category.id].orEmpty()
            val subcategoryHistories = categorySubcategories.mapNotNull { subcategory ->
                val subcategoryTransactions = transactionsBySubcategory[subcategory.id].orEmpty()

                if (subcategoryTransactions.isEmpty()) return@mapNotNull null

                SubcategoryHistory(
                    subcategory = subcategory,
                    transactionCount = subcategoryTransactions.size,
                    totalAmount = subcategoryTransactions.sumOf { it.amount },
                    transactions = subcategoryTransactions
                )
            }
                .sortedByDescending { it.totalAmount }

            CategoryHistory(
                category = category,
                transactionCount = categoryTransactions.size,
                totalAmount = categoryTransactions.sumOf { it.amount },
                subcategories = subcategoryHistories
            )
        }
            .sortedByDescending { it.totalAmount }
    }
}