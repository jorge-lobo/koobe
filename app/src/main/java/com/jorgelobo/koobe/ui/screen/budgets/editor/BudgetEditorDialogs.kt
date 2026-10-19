package com.jorgelobo.koobe.ui.screen.budgets.editor

import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.ui.components.composed.dialogs.DeleteDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.DiscardDialog
import com.jorgelobo.koobe.ui.components.model.enums.DeleteType
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction

@Composable
fun BudgetEditorDialogs(
    state: BudgetEditorUiState,
    onDiscardDialogAction: (ConfirmationDialogAction) -> Unit,
    onDeleteDialogAction: (ConfirmationDialogAction) -> Unit
) {
    if (state.discardDialog.visible) {
        DiscardDialog(
            onConfirm = { onDiscardDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { onDiscardDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }

    if (state.deleteDialog.visible) {
        DeleteDialog(
            type = DeleteType.BUDGET,
            onConfirm = { onDeleteDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { onDeleteDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }
}