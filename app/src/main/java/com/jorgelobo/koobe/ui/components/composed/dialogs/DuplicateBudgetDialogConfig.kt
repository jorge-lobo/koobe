package com.jorgelobo.koobe.ui.components.composed.dialogs

import androidx.compose.runtime.Stable

@Stable
data class DuplicateBudgetDialogConfig(
    val onContinueEditing: () -> Unit,
    val onDiscard: () -> Unit
)