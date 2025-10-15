package com.jorgelobo.koobe.ui.components.base.inputs.fields

import com.jorgelobo.koobe.ui.components.model.enums.InputState

data class InputFieldConfig(
    val value: String,
    val label: String,
    val placeholder: String? = null,
    val icon: String? = null,
    val isPassword: Boolean = false,
    val state: InputState = InputState.DEFAULT,
    val onValueChange: (String) -> Unit
)