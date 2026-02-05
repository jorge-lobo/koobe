package com.jorgelobo.koobe.di

import com.jorgelobo.koobe.data.preferences.AppPreferences
import com.jorgelobo.koobe.data.preferences.AppPreferencesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds
    @Singleton
    abstract fun bindAppPreferences(
        impl: AppPreferencesImpl
    ): AppPreferences
}