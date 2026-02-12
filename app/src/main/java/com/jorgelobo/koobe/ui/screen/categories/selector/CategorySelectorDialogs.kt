package com.jorgelobo.koobe.ui.screen.categories.selector

import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.ui.components.composed.dialogs.DiscardDialog
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction

@Composable
fun CategorySelectorDialogs(
    state: CategorySelectorUiState,
    onDiscardDialogAction: (ConfirmationDialogAction) -> Unit,
) {
    if (state.discardDialog.visible) {
        DiscardDialog(
            onConfirm = { onDiscardDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { onDiscardDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }
}