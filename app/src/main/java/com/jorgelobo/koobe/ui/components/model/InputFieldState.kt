package com.jorgelobo.koobe.ui.components.model

enum class InputState { DEFAULT, ERROR, DISABLED }

data class InputFieldState(
    val value: String,
    val label: String,
    val hint: String? = null,
    val icon: String? = null,
    val isPassword: Boolean = false,
    val state: InputState = InputState.DEFAULT,
    val onValueChange: (String) -> Unit
)