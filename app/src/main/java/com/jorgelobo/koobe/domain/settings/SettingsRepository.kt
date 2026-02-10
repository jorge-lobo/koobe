package com.jorgelobo.koobe.domain.settings

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.settings.UserSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun userSettings(): Flow<UserSettings>

    suspend fun setTheme(theme: ThemeOption)
    suspend fun setLanguage(language: AppLanguage)
    suspend fun setCurrency(currency: CurrencyType)
    suspend fun setStartOfWeek(startOfWeek: StartOfWeek)
    suspend fun setPaymentMethod(paymentMethod: PaymentMethodType)
}