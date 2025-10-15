package com.jorgelobo.koobe.ui.components.base.radioButtons

import com.jorgelobo.koobe.domain.model.constants.UiLabel
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.components.model.icons.IconPayment

data class RadioButtonConfig<T>(
    val options: List<T>,
    val selectedOption: T,
    val icons: Map<T, IconPayment> = emptyMap(),
    val state: UiState = UiState.ENABLED,
    val onSelectionChanged: (T) -> Unit
) where T: Enum<T>, T : UiLabel