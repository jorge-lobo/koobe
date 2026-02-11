package com.jorgelobo.koobe.di

import com.jorgelobo.koobe.data.settings.SettingsRepositoryImpl
import com.jorgelobo.koobe.domain.settings.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that binds the concrete implementation of [SettingsRepository] to its abstraction.
 *
 * Ensures that a singleton instance of [SettingsRepositoryImpl] is used whenever
 * [SettingsRepository] is requested.
 */
@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Provides
    @Singleton
    fun provideSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository = impl
}