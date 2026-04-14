package com.jorgelobo.koobe.data.repository

import com.jorgelobo.koobe.data.local.dao.TransactionDao
import com.jorgelobo.koobe.data.local.entity.TransactionEntity
import com.jorgelobo.koobe.data.mapper.toDomain
import com.jorgelobo.koobe.data.mapper.toEntity
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> =
        dao.getAll().map { list ->
            list.map { it.toDomain() }
        }

    override fun getTransactionsBySubcategoryId(subcategoryId: Int): Flow<List<Transaction>> =
        dao.getBySubcategoryId(subcategoryId).map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun getTransactionById(id: Int): Transaction? =
        dao.getById(id)?.toDomain()

    override suspend fun insertTransaction(transaction: Transaction) {
        dao.insert(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        dao.update(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.delete(transaction.toEntity())
    }

    override fun getTransactionsByPeriod(
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>> =
        dao.getTransactionsByPeriod(
            type,
            startDate,
            endDate
        ).toDomainList()

    override fun getTransactionsByPeriod(
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>> =
        dao.getTransactionsByPeriod(
            startDate,
            endDate
        ).toDomainList()

    override fun getTransactionByIdFlow(id: Int): Flow<Transaction?> =
        dao.getByIdFlow(id).map { it?.toDomain() }

    private fun Flow<List<TransactionEntity>>.toDomainList() =
        map { list -> list.map { it.toDomain() } }

    override suspend fun reassignSubcategory(
        oldSubcategoryId: Int,
        newSubcategoryId: Int,
        newCategoryId: Int
    ) {
        dao.reassignSubcategory(
            oldSubcategoryId,
            newSubcategoryId,
            newCategoryId
        )
    }

    override suspend fun reassignCategoryAndSubcategory(
        oldCategoryId: Int,
        newCategoryId: Int,
        newSubcategoryId: Int
    ) {
        dao.reassignCategoryAndSubcategory(
            oldCategoryId,
            newCategoryId,
            newSubcategoryId
        )
    }
}