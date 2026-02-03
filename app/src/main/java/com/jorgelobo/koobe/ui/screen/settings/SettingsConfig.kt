package com.jorgelobo.koobe.ui.screen.settings

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption

data class SettingsConfig(
    val currentRoute: String,
    val onRouteSelected: (String) -> Unit,
    val themeSelected: ThemeOption = ThemeOption.SYSTEM,
    val languageSelected: AppLanguage = AppLanguage.ENGLISH,
    val currencySelected: CurrencyType = CurrencyType.EUR,
    val daySelected: StartOfWeek = StartOfWeek.SUNDAY,
    val paymentMethodSelected: PaymentMethodType = PaymentMethodType.CASH
)