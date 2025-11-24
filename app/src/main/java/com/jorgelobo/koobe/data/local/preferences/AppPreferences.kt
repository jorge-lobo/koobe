package com.jorgelobo.koobe.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_prefs")

class AppPreferences(private val context: Context) {

    companion object {
        private val KEY_DEFAULTS_INSERTED = booleanPreferencesKey("defaults_inserted")
    }

    val defaultsInsertedFlow: Flow<Boolean>
        get() = context.dataStore.data.map { it[KEY_DEFAULTS_INSERTED] ?: false }

    suspend fun defaultsAlreadyInserted(): Boolean {
        return defaultsInsertedFlow.first()
    }

    suspend fun setDefaultsInserted() {
        context.dataStore.edit { prefs ->
            prefs[KEY_DEFAULTS_INSERTED] = true
        }
    }
}