package com.jorgelobo.koobe.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jorgelobo.koobe.data.local.entity.ShortcutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShortcutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shortcut: ShortcutEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<ShortcutEntity>)

    @Update
    suspend fun update(shortcut: ShortcutEntity)

    @Delete
    suspend fun delete(shortcut: ShortcutEntity)

    @Query("SELECT * FROM shortcuts ORDER BY name ASC")
    fun getAll(): Flow<List<ShortcutEntity>>

    @Query("SELECT * FROM shortcuts ORDER BY categoryId ASC")
    fun getAllOrderedByCategory(): Flow<List<ShortcutEntity>>

    @Query("SELECT * FROM shortcuts WHERE id = :id")
    suspend fun getById(id: Int): ShortcutEntity?

    @Query("SELECT * FROM shortcuts WHERE categoryId = :categoryId")
    suspend fun getByCategoryId(categoryId: Int): List<ShortcutEntity>
}