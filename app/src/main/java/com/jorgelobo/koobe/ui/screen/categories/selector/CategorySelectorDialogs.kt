package com.jorgelobo.koobe.ui.screen.categories.selector

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.ui.components.composed.dialogs.DiscardDialog
import com.jorgelobo.koobe.ui.components.composed.sheets.ShortcutActionBottomSheet
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.shortcutAction.ShortcutActionBottomSheetAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectorDialogs(
    state: CategorySelectorUiState,
    sheetState: SheetState,
    onDiscardDialogAction: (ConfirmationDialogAction) -> Unit,
    onShortcutAction: (ShortcutActionBottomSheetAction) -> Unit
) {
    if (state.discardDialog.visible) {
        DiscardDialog(
            onConfirm = { onDiscardDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { onDiscardDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }

    ShortcutActionBottomSheet(
        sheetState = sheetState,
        state = state.shortcutActionSheet,
        onAction = onShortcutAction
    )
}