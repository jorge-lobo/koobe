package com.jorgelobo.koobe.domain.repository

import com.jorgelobo.koobe.domain.model.transaction.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransactionBySubcategory(subcategoryId: Int): Flow<List<Transaction>>
    suspend fun getTransactionById(id: Int): Transaction?
    suspend fun insertTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
}