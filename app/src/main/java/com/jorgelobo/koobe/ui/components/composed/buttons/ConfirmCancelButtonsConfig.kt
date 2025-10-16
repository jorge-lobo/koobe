package com.jorgelobo.koobe.ui.components.composed.buttons

data class ConfirmCancelButtonsConfig(
    val confirmText: String,
    val cancelText: String,
    val isConfirmEnabled: Boolean = false,
    val onConfirmClick: () -> Unit,
    val onCancelClick: () -> Unit
)