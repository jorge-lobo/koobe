package com.jorgelobo.koobe.ui.components.composed.dialogs

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.ui.components.model.enums.OptionSelectorType

@Stable
data class OptionSelectorDialogConfig(
    val type: OptionSelectorType,
    val title: String,
    val selectedCurrency: CurrencyType? = null,
    val onCurrencySelected: (CurrencyType) -> Unit = {},
    val selectedWeekday: StartOfWeek? = null,
    val onWeekdaySelected: (StartOfWeek) -> Unit = {},
    val selectedLanguage: AppLanguage? = null,
    val onLanguageSelected: (AppLanguage) -> Unit = {},
    val selectedPeriod: PeriodType? = null,
    val onPeriodSelected: (PeriodType) -> Unit = {},
    val selectedPaymentMethod: PaymentMethodType? = null,
    val onPaymentMethodSelected: (PaymentMethodType) -> Unit = {},
    val onConfirm: () -> Unit,
    val onCancel: () -> Unit
)