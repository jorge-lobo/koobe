package com.jorgelobo.koobe.ui.components.base.inputs.fields

data class InputSelectorConfig(
    val value: String,
    val label: String,
    val onClick: () -> Unit
)