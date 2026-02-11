package com.jorgelobo.koobe.domain.model.settings

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption

/**
 * Represents the complete set of user preferences persisted by the application.
 *
 * This model is the single source of truth for user-configurable settings
 * such as theme, language, currency, week configuration and default payment method.
 */
data class UserSettings(
    val theme: ThemeOption,
    val language: AppLanguage,
    val currency: CurrencyType,
    val startOfWeek: StartOfWeek,
    val paymentMethod: PaymentMethodType
)