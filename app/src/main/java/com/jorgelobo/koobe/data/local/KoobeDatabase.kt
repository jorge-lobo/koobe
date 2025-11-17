package com.jorgelobo.koobe.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jorgelobo.koobe.data.local.converters.KoobeConverters
import com.jorgelobo.koobe.data.local.dao.*
import com.jorgelobo.koobe.data.local.entity.*

@Database(
    entities = [
        BudgetEntity::class,
        CategoryEntity::class,
        ShortcutEntity::class,
        SubcategoryEntity::class,
        TransactionEntity::class,
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(KoobeConverters::class)
abstract class KoobeDatabase : RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao
    abstract fun shortcutDao(): ShortcutDao
    abstract fun subcategoryDao(): SubcategoryDao
    abstract fun transactionDao(): TransactionDao
}