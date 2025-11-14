package com.jorgelobo.koobe.ui.components.composed.buttons

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class ConfirmCancelButtonsConfig(
    val confirmText: String,
    val cancelText: String,
    val confirmTextColor: Color? = null,
    val isConfirmEnabled: Boolean = false,
    val onConfirmClick: () -> Unit,
    val onCancelClick: () -> Unit
)