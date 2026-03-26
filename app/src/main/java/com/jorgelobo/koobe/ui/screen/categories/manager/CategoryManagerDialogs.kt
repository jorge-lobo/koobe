package com.jorgelobo.koobe.ui.screen.categories.manager

import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.ui.components.composed.dialogs.DeleteDialog
import com.jorgelobo.koobe.ui.components.model.enums.DeleteType
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction

@Composable
fun CategoryManagerDialogs(
    state: CategoryManagerUiState,
    onDeleteDialogAction: (ConfirmationDialogAction) -> Unit
) {

    if (state.deleteDialog.visible) {
        DeleteDialog(
            type = DeleteType.SUBCATEGORY,
            onConfirm = { onDeleteDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { onDeleteDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }
}