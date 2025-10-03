package com.jorgelobo.koobe.ui.components.model

import com.jorgelobo.koobe.domain.model.constants.UiLabel

enum class RadioButtonState { ENABLED, DISABLED }

data class RadioButtonConfig<T>(
    val options: List<T>,
    val selectedOption: T,
    val icons: Map<T, IconName> = emptyMap(),
    val state: RadioButtonState = RadioButtonState.ENABLED,
    val onSelectionChanged: (T) -> Unit
) where T: Enum<T>, T : UiLabel