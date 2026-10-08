package com.jorgelobo.koobe.ui.screen.budgets.editor.state

import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState

data class BudgetUiStateInternal(
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val hasTriedToSave: Boolean = false
)