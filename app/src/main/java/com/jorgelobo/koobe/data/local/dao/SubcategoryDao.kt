package com.jorgelobo.koobe.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jorgelobo.koobe.data.local.entity.SubcategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubcategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subcategory: SubcategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<SubcategoryEntity>)

    @Update
    suspend fun update(subcategory: SubcategoryEntity)

    @Delete
    suspend fun delete(subcategory: SubcategoryEntity)

    @Query("SELECT * FROM subcategories ORDER BY name ASC")
    fun getAll(): Flow<List<SubcategoryEntity>>

    @Query("SELECT * FROM subcategories WHERE id = :id")
    suspend fun getById(id: Int): SubcategoryEntity?

    @Query("SELECT * FROM subcategories WHERE categoryId = :categoryId")
    fun getByCategoryId(categoryId: Int): Flow<List<SubcategoryEntity>>

    @Query("SELECT * FROM subcategories WHERE id = :id")
    fun getByIdFlow(id: Int): Flow<SubcategoryEntity?>
}