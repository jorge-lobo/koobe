package com.jorgelobo.koobe.di

import android.content.Context
import androidx.room.Room
import com.jorgelobo.koobe.data.local.DatabaseInitializer
import com.jorgelobo.koobe.data.local.KoobeDatabase
import com.jorgelobo.koobe.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): KoobeDatabase {
        lateinit var database: KoobeDatabase

        database = Room.databaseBuilder(
            context,
            KoobeDatabase::class.java,
            "koobe_db"
        )
            .addCallback(
                DatabaseInitializer(
                    categoryDaoProvider = { database.categoryDao() },
                    subcategoryDaoProvider = { database.subcategoryDao() }
                )
            )
            .build()

        return database
    }

    @Provides
    fun provideBudgetDao(db: KoobeDatabase): BudgetDao = db.budgetDao()

    @Provides
    fun provideCategoryDao(db: KoobeDatabase): CategoryDao = db.categoryDao()

    @Provides
    fun provideShortcutDao(db: KoobeDatabase): ShortcutDao = db.shortcutDao()

    @Provides
    fun provideSubcategoryDao(db: KoobeDatabase): SubcategoryDao = db.subcategoryDao()

    @Provides
    fun provideTransactionDao(db: KoobeDatabase): TransactionDao = db.transactionDao()
}