package com.jorgelobo.koobe.ui.components.base.radioButtons

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.SortingType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

fun paymentMethodRadioButtonConfig(
    selected: PaymentMethodType,
    onOptionSelected: (PaymentMethodType) -> Unit
): RadioButtonConfig<PaymentMethodType> {
    val iconMap = PaymentMethodType.entries.associateWith { IconPack.valueOf(it.name) }

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

fun periodRadioButtonConfig(
    selected: PeriodType,
    onOptionSelected: (PeriodType) -> Unit
): RadioButtonConfig<PeriodType> {
    return RadioButtonConfig(
        options = PeriodType.entries,
        selectedOption = selected,
        state = UiState.ENABLED,
        onSelectionChanged = onOptionSelected
    )
}

fun periodFilterRadioButtonConfig(
    selected: PeriodType,
    onOptionSelected: (PeriodType) -> Unit
): RadioButtonConfig<PeriodType> {
    return RadioButtonConfig(
        options = listOf(PeriodType.YEARLY, PeriodType.MONTHLY),
        selectedOption = selected,
        state = UiState.ENABLED,
        onSelectionChanged = onOptionSelected
    )
}

fun sortingRadioButtonConfig(
    selected: SortingType,
    onOptionSelected: (SortingType) -> Unit
): RadioButtonConfig<SortingType> {
    return RadioButtonConfig(
        options = SortingType.entries,
        selectedOption = selected,
        state = UiState.ENABLED,
        onSelectionChanged = onOptionSelected
    )
}