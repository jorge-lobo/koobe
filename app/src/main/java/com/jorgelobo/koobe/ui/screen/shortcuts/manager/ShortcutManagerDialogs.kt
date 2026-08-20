package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.SortingType
import com.jorgelobo.koobe.ui.components.composed.dialogs.DeleteDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.DisableDialog
import com.jorgelobo.koobe.ui.components.composed.sheets.ListSelectorBottomSheet
import com.jorgelobo.koobe.ui.components.composed.sheets.ListSelectorBottomSheetConfig
import com.jorgelobo.koobe.ui.components.composed.sheets.ShortcutRecurrenceBottomSheet
import com.jorgelobo.koobe.ui.components.model.enums.DeleteType
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.shortcutRecurrence.ShortcutRecurrenceBottomSheetAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction

/**
 * Composable function that manages the display of overlay UI elements such as dialogs and
 * bottom sheets for the Shortcut Manager screen.
 *
 * @param state The current UI state containing visibility flags and data for the dialogs.
 * @param sortingSheetState The state of the sorting bottom sheet.
 * @param recurrenceSheetState The state of the shortcut recurrence bottom sheet.
 * @param periodSheetState The state of the period selector bottom sheet.
 * @param onDeleteDialogAction Callback handled when a user interacts with the delete confirmation dialog.
 * @param onDisableDialogAction Callback handled when a user interacts with the disable confirmation dialog.
 * @param onSortingDialogAction Callback handled when a user selects a sorting option or dismisses the sorting sheet.
 * @param onShortcutRecurrenceAction Callback handled when a user interacts with the shortcut recurrence bottom sheet.
 * @param onPeriodSelectorAction Callback handled when a user selects a period option or dismisses the period selector sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortcutManagerDialogs(
    state: ShortcutManagerUiState,
    sortingSheetState: SheetState,
    recurrenceSheetState: SheetState,
    periodSheetState: SheetState,
    onDeleteDialogAction: (ConfirmationDialogAction) -> Unit,
    onDisableDialogAction: (ConfirmationDialogAction) -> Unit,
    onSortingDialogAction: (SelectorSheetAction<SortingType>) -> Unit,
    onShortcutRecurrenceAction: (ShortcutRecurrenceBottomSheetAction) -> Unit,
    onPeriodSelectorAction: (SelectorSheetAction<PeriodType>) -> Unit
) {

    if (state.deleteDialog.visible) {
        DeleteDialog(
            type = DeleteType.SHORTCUT,
            onConfirm = { onDeleteDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { onDeleteDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }

    if (state.disableDialog.visible) {
        DisableDialog(
            onConfirm = { onDisableDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { onDisableDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }

    if (state.sortingSelector.visible) {
        ModalBottomSheet(
            onDismissRequest = { onSortingDialogAction(SelectorSheetAction.Dismiss) }
        ) {
            ListSelectorBottomSheet(
                sheetState = sortingSheetState,
                config = ListSelectorBottomSheetConfig.Sorting(
                    selected = state.sortingSelector.selected,
                    onItemSelected = { onSortingDialogAction(SelectorSheetAction.Select(it)) }
                ),
                onDismiss = { onSortingDialogAction(SelectorSheetAction.Dismiss) }
            )
        }
    }

    if (state.periodSelector.visible) {
        ModalBottomSheet(
            onDismissRequest = { onPeriodSelectorAction(SelectorSheetAction.Dismiss) }
        ) {
            ListSelectorBottomSheet(
                sheetState = periodSheetState,
                config = ListSelectorBottomSheetConfig.Period(
                    selected = state.periodSelector.selected,
                    onItemSelected = { onPeriodSelectorAction(SelectorSheetAction.Select(it)) }
                ),
                onDismiss = { onPeriodSelectorAction(SelectorSheetAction.Dismiss) }
            )
        }
    }

    ShortcutRecurrenceBottomSheet(
        sheetState = recurrenceSheetState,
        state = state.shortcutRecurrenceSheet,
        onAction = onShortcutRecurrenceAction
    )
}