package com.jorgelobo.koobe.data.preferences

import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import kotlinx.coroutines.flow.Flow

interface AppPreferences {
    fun themeOption(): Flow<ThemeOption>
    suspend fun setThemeOption(option: ThemeOption)
}