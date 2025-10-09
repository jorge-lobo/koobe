package com.jorgelobo.koobe.ui.components.model

data class InputSelectorConfig(
    val value: String,
    val label: String,
    val onClick: () -> Unit
)