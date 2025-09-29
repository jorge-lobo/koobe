package com.jorgelobo.koobe.ui.components.model

import com.jorgelobo.koobe.domain.model.constants.ToggleLabel

enum class ToggleState { ENABLED, DISABLED }

data class ToggleConfig<T>(
    val options: List<T>,
    val selectedOption: T,
    val state: ToggleState = ToggleState.ENABLED,
    val onSelectionChanged: (T) -> Unit
) where T : Enum<T>, T : ToggleLabel