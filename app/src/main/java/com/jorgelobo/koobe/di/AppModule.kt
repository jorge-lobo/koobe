package com.jorgelobo.koobe.di

import android.content.Context
import com.jorgelobo.koobe.data.local.preferences.AppPreferences
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.domain.usecase.app.AppStartUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppPreferences(@ApplicationContext context: Context): AppPreferences =
        AppPreferences(context)

    @Provides
    @Singleton
    fun provideAppStartUseCase(
        preferences: AppPreferences,
        categoryRepository: CategoryRepository,
        subcategoryRepository: SubcategoryRepository
    ): AppStartUseCase {
        return AppStartUseCase(preferences, categoryRepository, subcategoryRepository)
    }
}