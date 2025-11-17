package com.jorgelobo.koobe.di

import com.jorgelobo.koobe.data.local.dao.*
import com.jorgelobo.koobe.data.local.datasource.LocalDataSource
import com.jorgelobo.koobe.data.local.datasource.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideLocalDataSource(
        budgetDao: BudgetDao,
        categoryDao: CategoryDao,
        shortcutDao: ShortcutDao,
        subcategoryDao: SubcategoryDao,
        transactionDao: TransactionDao
    ): LocalDataSource = LocalDataSourceImpl(
        budgetDao,
        categoryDao,
        shortcutDao,
        subcategoryDao,
        transactionDao
    )
}