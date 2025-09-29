package com.jorgelobo.koobe.ui.components.model

data class SwitchConfig(
    val checked: Boolean,
    val state: ButtonState = ButtonState.ENABLED,
    val onCheckedChange: (Boolean) -> Unit
)
