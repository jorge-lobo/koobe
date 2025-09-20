package com.jorgelobo.koobe.ui.components.model

enum class ButtonType { PRIMARY, SECONDARY, SQUARE, TEXT, ICON, TOGGLE }
enum class ButtonState { ENABLED, DISABLED, LOADING }

data class ButtonConfig(
    val text: String,
    val type: ButtonType,
    val state: ButtonState = ButtonState.ENABLED,
    val icon: String? = null,
    val onClick: () -> Unit
)