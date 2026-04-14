package com.jorgelobo.koobe.domain.usecase.historic

import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.CategoryHistory
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.model.subcategory.SubcategoryHistory
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

/**
 * Use case responsible for retrieving and aggregating historical transaction data.
 *
 * This class calculates the total amounts and transaction counts grouped by categories and
 * subcategories for a specific period and transaction type. It filters out categories and
 * subcategories that have no transactions within the specified timeframe and sorts the results
 * by the total amount in descending order.
 *
 * @property getCategoriesByType Use case to retrieve categories filtered by transaction type.
 * @property getAllSubcategories Use case to retrieve all available subcategories.
 * @property transactionRepository Repository to fetch transaction records from the data source.
 */
class GetHistoricDataUseCase @Inject constructor(
    private val getCategoriesByType: GetCategoriesByTransactionTypeUseCase,
    private val getAllSubcategories: GetAllSubcategoriesUseCase,
    private val transactionRepository: TransactionRepository
) {

    /**
     * Retrieves the historical financial data grouped by category and subcategory for a specific period.
     *
     * @param date The reference date used to determine the start and end of the period.
     * @param transactionType The type of transactions to filter (e.g., INCOME or EXPENSE).
     * @param periodType The granularity of the period (e.g., MONTHLY, YEARLY).
     * @return A [Flow] emitting a list of [CategoryHistory] objects, containing transaction counts,
     */
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

    /**
     * Transforms raw category, subcategory, and transaction data into a hierarchical list of [CategoryHistory].
     *
     * This method groups transactions by their respective categories and subcategories, calculates
     * the total amount and transaction count for each, and filters out any categories or
     * subcategories that do not have associated transactions in the provided list.
     *
     * @param categories The list of categories to be processed.
     */
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

            // Ignore categories without transactions
            if (categoryTransactions.isEmpty()) return@mapNotNull null

            val categorySubcategories = subcategoriesByCategory[category.id].orEmpty()
            val subcategoryHistories = categorySubcategories.mapNotNull { subcategory ->
                val subcategoryTransactions = transactionsBySubcategory[subcategory.id].orEmpty()

                // Ignore subcategories without transactions
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