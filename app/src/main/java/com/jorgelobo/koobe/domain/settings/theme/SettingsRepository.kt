package com.jorgelobo.koobe.domain.settings.theme

import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun themeOption(): Flow<ThemeOption>
    suspend fun saveThemeOption(option: ThemeOption)
}