package com.jorgelobo.koobe.ui.components.base.toggles

import com.jorgelobo.koobe.domain.model.constants.enums.CategoryDetailType
import com.jorgelobo.koobe.domain.model.constants.enums.MetricType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.model.enums.UiState

fun transactionToggleConfig(
    selected: TransactionType,
    onOptionSelected: (TransactionType) -> Unit
): ToggleConfig<TransactionType> {
    return ToggleConfig(
        options = TransactionType.entries,
        selectedOption = selected,
        state = UiState.ENABLED,
        onSelectionChanged = onOptionSelected
    )
}

fun metricToggleConfig(
    selected: MetricType,
    onOptionSelected: (MetricType) -> Unit
): ToggleConfig<MetricType> {
    return ToggleConfig(
        options = MetricType.entries,
        selectedOption = selected,
        state = UiState.ENABLED,
        onSelectionChanged = onOptionSelected
    )
}

fun periodToggleConfig(
    selected: PeriodType,
    onOptionSelected: (PeriodType) -> Unit
): ToggleConfig<PeriodType> {
    return ToggleConfig(
        options = PeriodType.entries,
        selectedOption = selected,
        state = UiState.ENABLED,
        onSelectionChanged = onOptionSelected
    )
}

fun themeToggleConfig(
    selected: ThemeOption,
    onOptionSelected: (ThemeOption) -> Unit
): ToggleConfig<ThemeOption> {
    return ToggleConfig(
        options = ThemeOption.entries,
        selectedOption = selected,
        state = UiState.ENABLED,
        onSelectionChanged = onOptionSelected
    )
}

fun categoryDetailToggleConfig(
    selected: CategoryDetailType,
    onOptionSelected: (CategoryDetailType) -> Unit
): ToggleConfig<CategoryDetailType> {
    return ToggleConfig(
        options = CategoryDetailType.entries,
        selectedOption = selected,
        state = UiState.ENABLED,
        onSelectionChanged = onOptionSelected
    )
}