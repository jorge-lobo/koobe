package com.jorgelobo.koobe.domain.model.settings

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption

/**
 * Represents the complete set of user preferences persisted by the application.
 *
 * This model is the single source of truth for user-configurable settings, including theme,
 * language, currency, start of week and default payment method.
 */
data class UserSettings(
    val theme: ThemeOption,
    val language: AppLanguage,
    val currency: CurrencyType,
    val startOfWeek: StartOfWeek,
    val paymentMethod: PaymentMethodType
)

/**
 * Provides the default user settings used when no persisted preferences are available.
 */
val DefaultUserSettings = UserSettings(
    theme = ThemeOption.SYSTEM,
    language = AppLanguage.ENGLISH,
    currency = CurrencyType.EUR,
    startOfWeek = StartOfWeek.SUNDAY,
    paymentMethod = PaymentMethodType.CASH
)