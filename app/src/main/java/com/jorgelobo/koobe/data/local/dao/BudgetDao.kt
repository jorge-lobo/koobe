package com.jorgelobo.koobe.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jorgelobo.koobe.data.local.entity.BudgetEntity
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: BudgetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<BudgetEntity>)

    @Update
    suspend fun update(budget: BudgetEntity)

    @Delete
    suspend fun delete(budget: BudgetEntity)

    @Query("SELECT * FROM budgets ORDER BY id ASC")
    fun getAll(): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getById(id: Int): BudgetEntity?

    @Query("SELECT * FROM budgets WHERE categoryId = :categoryId")
    suspend fun getByCategoryId(categoryId: Int): List<BudgetEntity>

    @Query("SELECT * FROM budgets WHERE subcategoryId = :subcategoryId")
    suspend fun getBySubcategoryId(subcategoryId: Int): List<BudgetEntity>

    @Query("SELECT * FROM budgets WHERE period = :period")
    suspend fun getByPeriod(period: PeriodType): List<BudgetEntity>
}