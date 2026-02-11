package com.jorgelobo.koobe.data.settings

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.settings.SettingsRepository
import com.jorgelobo.koobe.domain.model.settings.UserSettings
import javax.inject.Inject

/**
 * Implementation of [SettingsRepository] that manages user preferences using [SettingsDataStore].
 *
 * This repository acts as a bridge between the domain layer and the data persistence layer,
 * providing a reactive stream of [UserSettings] and methods to update various application
 * configurations such as theme, language, currency, and other localization settings.
 *
 * @property dataStore The data source responsible for persisting and retrieving setting values.
 */
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: SettingsDataStore
) : SettingsRepository {

    override fun userSettings() = dataStore.userSettingsFlow

    override suspend fun setTheme(theme: ThemeOption) {
        dataStore.setTheme(theme)
    }

    override suspend fun setLanguage(language: AppLanguage) {
        dataStore.setLanguage(language)
    }

    override suspend fun setCurrency(currency: CurrencyType) {
        dataStore.setCurrency(currency)
    }

    override suspend fun setStartOfWeek(startOfWeek: StartOfWeek) {
        dataStore.setStartOfWeek(startOfWeek)
    }

    override suspend fun setPaymentMethod(paymentMethod: PaymentMethodType) {
        dataStore.setPaymentMethod(paymentMethod)
    }
}