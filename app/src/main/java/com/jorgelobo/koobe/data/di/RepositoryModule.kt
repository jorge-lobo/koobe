package com.jorgelobo.koobe.data.di

import com.jorgelobo.koobe.data.repository.BudgetRepositoryImpl
import com.jorgelobo.koobe.data.repository.CategoryRepositoryImpl
import com.jorgelobo.koobe.data.repository.ShortcutRepositoryImpl
import com.jorgelobo.koobe.data.repository.SubcategoryRepositoryImpl
import com.jorgelobo.koobe.data.repository.TransactionRepositoryImpl
import com.jorgelobo.koobe.domain.repository.BudgetRepository
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    abstract fun bindSubcategoryRepository(
        impl: SubcategoryRepositoryImpl
    ): SubcategoryRepository

    @Binds
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    abstract fun bindShortcutRepository(
        impl: ShortcutRepositoryImpl
    ): ShortcutRepository

    @Binds
    abstract fun bindBudgetRepository(
        impl: BudgetRepositoryImpl
    ): BudgetRepository
}