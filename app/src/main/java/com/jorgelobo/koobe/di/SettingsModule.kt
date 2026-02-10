package com.jorgelobo.koobe.di

import android.content.Context
import com.jorgelobo.koobe.data.settings.SettingsDataStore
import com.jorgelobo.koobe.data.settings.SettingsRepositoryImpl
import com.jorgelobo.koobe.domain.settings.theme.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ) = SettingsDataStore(context)

    @Provides
    @Singleton
    fun provideSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository = impl
}