package com.jorgelobo.koobe.data.settings

import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: SettingsDataStore
) : SettingsRepository{

    override fun themeOption(): Flow<ThemeOption> =
        dataStore.themeOptionFlow()

    override suspend fun saveThemeOption(option: ThemeOption) {
        dataStore.saveThemeOption(option)
    }
}