package com.jorgelobo.koobe.ui.components.composed.dialogs

import com.jorgelobo.koobe.ui.components.model.enums.DialogType

data class DialogConfig(
    val title: String,
    val message: String? = null,
    val icon: String? = null,
    val type: DialogType = DialogType.DISCARD,
    val confirmText: String,
    val cancelText: String,
    val onConfirm: () -> Unit,
    val onCancel: () -> Unit
)