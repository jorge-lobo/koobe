package com.jorgelobo.koobe.ui.components.base.toggles

import com.jorgelobo.koobe.domain.model.constants.UiLabel
import com.jorgelobo.koobe.ui.components.model.enums.UiState

data class ToggleConfig<T>(
    val options: List<T>,
    val selectedOption: T,
    val state: UiState = UiState.ENABLED,
    val onSelectionChanged: (T) -> Unit
) where T : Enum<T>, T : UiLabel