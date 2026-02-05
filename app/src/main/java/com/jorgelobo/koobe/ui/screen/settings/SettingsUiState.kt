package com.jorgelobo.koobe.ui.screen.settings

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek

data class SettingsUiState(
    val languageSelected: AppLanguage = AppLanguage.ENGLISH,
    val currencySelected: CurrencyType = CurrencyType.EUR,
    val startOfWeekSelected: StartOfWeek = StartOfWeek.SUNDAY,
    val paymentMethodSelected: PaymentMethodType = PaymentMethodType.CASH,
    val activeDialog: SettingsDialog? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface SettingsDialog {
    data object LanguageSelector : SettingsDialog
    data object CurrencySelector : SettingsDialog
    data object StartOfWeekSelector : SettingsDialog
    data object PaymentMethodSelector : SettingsDialog
}