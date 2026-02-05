package com.jorgelobo.koobe.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class SettingsDataStore(
    private val context: Context
) {
    private val themeOptionKey = stringPreferencesKey("theme_option")

    fun themeOptionFlow(): Flow<ThemeOption> =
        context.dataStore.data.map { prefs ->
            ThemeOption.valueOf(
                prefs[themeOptionKey] ?: ThemeOption.SYSTEM.name
            )
        }

    suspend fun saveThemeOption(option: ThemeOption) {
        context.dataStore.edit { prefs ->
            prefs[themeOptionKey] = option.name
        }
    }
}