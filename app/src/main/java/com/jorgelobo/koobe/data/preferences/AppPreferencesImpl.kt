package com.jorgelobo.koobe.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferencesImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AppPreferences {

    private val themeOptionKey = stringPreferencesKey("theme_option")

    override fun themeOption(): Flow<ThemeOption> =
        dataStore.data.map { prefs ->
            prefs[themeOptionKey]
                ?.let { ThemeOption.valueOf(it) }
                ?: ThemeOption.SYSTEM
        }

    override suspend fun setThemeOption(option: ThemeOption) {
        dataStore.edit { prefs ->
            prefs[themeOptionKey] = option.name
        }
    }
}