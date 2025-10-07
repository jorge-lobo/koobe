package com.jorgelobo.koobe.ui.components.model

import com.jorgelobo.koobe.domain.model.constants.UiState

data class SwitchConfig(
    val checked: Boolean,
    val state: UiState = UiState.ENABLED,
    val onCheckedChange: (Boolean) -> Unit
)
