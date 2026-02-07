package com.jorgelobo.koobe.ui.screen.settings

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState

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