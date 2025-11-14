package com.jorgelobo.koobe.ui.components.base.switchButton

import com.jorgelobo.koobe.ui.components.model.enums.UiState

data class SwitchConfig(
    val checked: Boolean,
    val state: UiState = UiState.ENABLED,
    val onCheckedChange: (Boolean) -> Unit
)