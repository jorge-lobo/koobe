package com.jorgelobo.koobe.ui.components.composed.dialogs

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.ui.components.model.enums.AvatarConfigurationType

@Stable
data class AvatarConfigurationDialogConfig(
    val type: AvatarConfigurationType,
    val onSelection: (String) -> Unit,
    val onApply: () -> Unit,
    val onCancel: () -> Unit
)