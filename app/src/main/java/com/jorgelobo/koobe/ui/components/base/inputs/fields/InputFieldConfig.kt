package com.jorgelobo.koobe.ui.components.base.inputs.fields

import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class InputFieldConfig(
    val value: String,
    val label: String,
    val placeholder: String? = null,
    val icon: IconPack? = null,
    val isPassword: Boolean = false,
    val state: InputState = InputState.DEFAULT,
    val onValueChange: (String) -> Unit,
    val onResetClick: () -> Unit
)