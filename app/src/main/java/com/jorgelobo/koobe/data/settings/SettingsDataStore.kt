package com.jorgelobo.koobe.data.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.settings.UserSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore("settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val KEY_THEME = stringPreferencesKey("theme")
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_CURRENCY = stringPreferencesKey("currency")
        private val KEY_START_OF_WEEK = stringPreferencesKey("start_of_week")
        private val KEY_PAYMENT_METHOD = stringPreferencesKey("payment_method")
    }

    val userSettingsFlow: Flow<UserSettings> =
        dataStore.data.map { prefs ->
            UserSettings(
                theme = prefs[KEY_THEME]?.let(ThemeOption::valueOf) ?: ThemeOption.SYSTEM,
                language = prefs[KEY_LANGUAGE]?.let(AppLanguage::valueOf) ?: AppLanguage.ENGLISH,
                currency = prefs[KEY_CURRENCY]?.let(CurrencyType::valueOf) ?: CurrencyType.EUR,
                startOfWeek = prefs[KEY_START_OF_WEEK]?.let(StartOfWeek::valueOf)
                    ?: StartOfWeek.SUNDAY,
                paymentMethod = prefs[KEY_PAYMENT_METHOD]?.let(PaymentMethodType::valueOf)
                    ?: PaymentMethodType.CASH
            )
        }.distinctUntilChanged()

    suspend fun setTheme(value: ThemeOption) = edit(KEY_THEME, value.name)
    suspend fun setLanguage(value: AppLanguage) = edit(KEY_LANGUAGE, value.name)
    suspend fun setCurrency(value: CurrencyType) = edit(KEY_CURRENCY, value.name)
    suspend fun setStartOfWeek(value: StartOfWeek) = edit(KEY_START_OF_WEEK, value.name)
    suspend fun setPaymentMethod(value: PaymentMethodType) = edit(KEY_PAYMENT_METHOD, value.name)

    private suspend fun edit(key: Preferences.Key<String>, value: String) {
        dataStore.edit { it[key] = value }
    }
}