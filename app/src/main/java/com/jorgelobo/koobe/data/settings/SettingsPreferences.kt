package com.jorgelobo.koobe.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsPreferences @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore = context.dataStore

    companion object {
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_CURRENCY = stringPreferencesKey("currency")
        private val KEY_START_OF_WEEK = stringPreferencesKey("start_of_week")
        private val KEY_PAYMENT_METHOD = stringPreferencesKey("payment_method")

    }

    val languageFlow: Flow<AppLanguage> =
        dataStore.data
            .map { prefs ->
                prefs[KEY_LANGUAGE]?.let(AppLanguage::valueOf)
                    ?: AppLanguage.ENGLISH
            }
            .distinctUntilChanged()

    val currencyFlow: Flow<CurrencyType> =
        dataStore.data
            .map { prefs ->
                prefs[KEY_CURRENCY]?.let(CurrencyType::valueOf)
                    ?: CurrencyType.EUR
            }
            .distinctUntilChanged()

    val startOfWeekFlow: Flow<StartOfWeek> =
        dataStore.data
            .map { prefs ->
                prefs[KEY_START_OF_WEEK]?.let(StartOfWeek::valueOf)
                    ?: StartOfWeek.SUNDAY
            }
            .distinctUntilChanged()

    val paymentMethodFlow: Flow<PaymentMethodType> =
        dataStore.data
            .map { prefs ->
                prefs[KEY_PAYMENT_METHOD]?.let(PaymentMethodType::valueOf)
                    ?: PaymentMethodType.CASH
            }
            .distinctUntilChanged()

    suspend fun setLanguage(language: AppLanguage) {
        dataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = language.name
        }
    }

    suspend fun setCurrency(currency: CurrencyType) {
        dataStore.edit { prefs ->
            prefs[KEY_CURRENCY] = currency.name
        }
    }

    suspend fun setStartOfWeek(startOfWeek: StartOfWeek) {
        dataStore.edit { prefs ->
            prefs[KEY_START_OF_WEEK] = startOfWeek.name
        }
    }

    suspend fun setPaymentMethod(paymentMethod: PaymentMethodType) {
        dataStore.edit { prefs ->
            prefs[KEY_PAYMENT_METHOD] = paymentMethod.name
        }
    }
}