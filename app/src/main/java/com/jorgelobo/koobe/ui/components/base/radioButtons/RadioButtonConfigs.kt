package com.jorgelobo.koobe.ui.components.base.radioButtons

import com.jorgelobo.koobe.domain.model.constants.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.UiState
import com.jorgelobo.koobe.ui.components.model.RadioButtonConfig
import com.jorgelobo.koobe.ui.components.model.icons.IconPayment

fun paymentMethodRadioButtonConfig(
    selected: PaymentMethodType,
    onOptionSelected: (PaymentMethodType) -> Unit
): RadioButtonConfig<PaymentMethodType> {
    val iconMap = PaymentMethodType.entries.associateWith { IconPayment.valueOf(it.name) }

    return RadioButtonConfig(
        options = PaymentMethodType.entries,
        selectedOption = selected,
        icons = iconMap,
        state = UiState.ENABLED,
        onSelectionChanged = onOptionSelected
    )
}

fun currencyRadioButtonConfig(
    selected: CurrencyType,
    onOptionSelected: (CurrencyType) -> Unit
): RadioButtonConfig<CurrencyType> {
    return RadioButtonConfig(
        options = CurrencyType.entries,
        selectedOption = selected,
        state = UiState.ENABLED,
        onSelectionChanged = onOptionSelected
    )
}

fun startOfWeekRadioButtonConfig(
    selected: StartOfWeek,
    onOptionSelected: (StartOfWeek) -> Unit
): RadioButtonConfig<StartOfWeek> {
    return RadioButtonConfig(
        options = StartOfWeek.entries,
        selectedOption = selected,
        state = UiState.ENABLED,
        onSelectionChanged = onOptionSelected
    )
}

fun languageRadioButtonConfig(
    selected: AppLanguage,
    onOptionSelected: (AppLanguage) -> Unit
): RadioButtonConfig<AppLanguage> {
    return RadioButtonConfig(
        options = AppLanguage.entries,
        selectedOption = selected,
        state = UiState.ENABLED,
        onSelectionChanged = onOptionSelected
    )
}