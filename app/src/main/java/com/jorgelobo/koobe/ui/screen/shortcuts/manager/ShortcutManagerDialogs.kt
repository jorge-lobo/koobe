package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.domain.model.constants.enums.SortingType
import com.jorgelobo.koobe.ui.components.composed.dialogs.DeleteDialog
import com.jorgelobo.koobe.ui.components.composed.sheets.ListSelectorBottomSheet
import com.jorgelobo.koobe.ui.components.composed.sheets.ListSelectorBottomSheetConfig
import com.jorgelobo.koobe.ui.components.model.enums.DeleteType
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortcutManagerDialogs(
    state: ShortcutManagerUiState,
    sheetState: SheetState,
    onDeleteDialogAction: (ConfirmationDialogAction) -> Unit,
    onSortingDialogAction: (SelectorSheetAction<SortingType>) -> Unit
) {

    if (state.deleteDialog.visible) {
        DeleteDialog(
            type = DeleteType.SHORTCUT,
            onConfirm = { onDeleteDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { onDeleteDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }

    if (state.sortingSelector.visible) {
        ModalBottomSheet(
            onDismissRequest = { onSortingDialogAction(SelectorSheetAction.Dismiss) }
        ) {
            ListSelectorBottomSheet(
                sheetState = sheetState,
                config = ListSelectorBottomSheetConfig.Sorting(
                    selected = state.sortingSelector.selected,
                    onItemSelected = { onSortingDialogAction(SelectorSheetAction.Select(it)) }
                ),
                onDismiss = { onSortingDialogAction(SelectorSheetAction.Dismiss) }
            )
        }
    }
}