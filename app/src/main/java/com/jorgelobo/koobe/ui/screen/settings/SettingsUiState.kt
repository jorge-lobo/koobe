package com.jorgelobo.koobe.ui.screen.settings

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState

/**
 * Represents the UI state for the settings screen.
 *
 * @property languageSelected The currently selected application language.
 * @property currencySelected The currently selected currency type for financial displays.
 * @property startOfWeekSelected The chosen first day of the week (e.g., Sunday or Monday).
 * @property paymentMethodSelected The default payment method selected by the user.
 * @property languageDialog State management for the language selection dialog.
 * @property currencyDialog State management for the currency selection dialog.
 * @property startOfWeekDialog State management for the start-of-week selection dialog.
 * @property paymentMethodDialog State management for the payment method selection dialog.
 * @property isLoading Indicates whether settings data is currently being loaded or processed.
 * @property errorMessage Contains an error message if an operation fails, otherwise null.
 */
data class SettingsUiState(
    val languageSelected: AppLanguage = AppLanguage.ENGLISH,
    val currencySelected: CurrencyType = CurrencyType.EUR,
    val startOfWeekSelected: StartOfWeek = StartOfWeek.SUNDAY,
    val paymentMethodSelected: PaymentMethodType = PaymentMethodType.CASH,
    val languageDialog: SelectorDialogState<AppLanguage> = SelectorDialogState(initial = AppLanguage.ENGLISH),
    val currencyDialog: SelectorDialogState<CurrencyType> = SelectorDialogState(initial = CurrencyType.EUR),
    val startOfWeekDialog: SelectorDialogState<StartOfWeek> = SelectorDialogState(initial = StartOfWeek.SUNDAY),
    val paymentMethodDialog: SelectorDialogState<PaymentMethodType> = SelectorDialogState(initial = PaymentMethodType.CASH),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)