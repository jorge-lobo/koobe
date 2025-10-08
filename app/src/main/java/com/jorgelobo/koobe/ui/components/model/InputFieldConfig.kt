package com.jorgelobo.koobe.ui.components.model

enum class InputState { DEFAULT, ERROR }

data class InputFieldConfig(
    val value: String,
    val label: String,
    val placeholder: String? = null,
    val icon: String? = null,
    val isPassword: Boolean = false,
    val state: InputState = InputState.DEFAULT,
    val onValueChange: (String) -> Unit
)