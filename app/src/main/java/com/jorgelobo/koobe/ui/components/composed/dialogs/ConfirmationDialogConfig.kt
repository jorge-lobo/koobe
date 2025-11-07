package com.jorgelobo.koobe.ui.components.composed.dialogs

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.ui.components.model.enums.ConfirmationType

@Stable
data class ConfirmationDialogConfig(
    val type: ConfirmationType,
    val title: String,
    val message: String,
    val helperText: String? = null,
    val onConfirm: () -> Unit,
    val onCancel: () -> Unit
)