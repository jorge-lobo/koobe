package com.jorgelobo.koobe.ui.screen.budgets.editor

import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.ui.components.composed.dialogs.DeleteDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.DiscardDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.DuplicateBudgetDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.DuplicateBudgetDialogConfig
import com.jorgelobo.koobe.ui.components.composed.dialogs.ExistingBudgetsDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.ExistingBudgetsDialogConfig
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.components.model.enums.DeleteType
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction

/**
 * Composable function that manages and displays various dialogs and bottom sheets used within the
 * Budget Editor screen.
 *
 * It observes the [state] to determine which UI component (discard dialog, delete dialog,
 * icon selector, currency selector, or list sheets) should be visible and handles the respective
 * user actions.
 *
 * @param state The current UI state containing visibility flags and data for the dialogs.
 * @param onDiscardDialogAction Callback invoked when an action is taken in the discard changes dialog.
 * @param onDeleteDialogAction Callback invoked when an action is taken in the delete confirmation dialog.
 * @param onDuplicateDialogAction Callback invoked when an action is taken in the duplicate budget dialog.
 * @param onContinueCreatingBudget Callback invoked when the user chooses to continue creating a budget despite existing entries.
 * @param onCancelExistingBudgetsDialog Callback invoked to dismiss the existing budgets dialog.
 */
@Composable
fun BudgetEditorDialogs(
    state: BudgetEditorUiState,
    onDiscardDialogAction: (ConfirmationDialogAction) -> Unit,
    onDeleteDialogAction: (ConfirmationDialogAction) -> Unit,
    onDuplicateDialogAction: (ConfirmationDialogAction) -> Unit,
    onContinueCreatingBudget: () -> Unit,
    onCancelExistingBudgetsDialog: () -> Unit,
    onExistingBudgetClick: (BudgetUiModel) -> Unit
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

    if (state.duplicateBudgetDialog.visible) {
        DuplicateBudgetDialog(
            config = DuplicateBudgetDialogConfig(
                onContinueEditing = { onDuplicateDialogAction(ConfirmationDialogAction.Dismiss) },
                onDiscard = { onDuplicateDialogAction(ConfirmationDialogAction.Confirm) }
            )
        )
    }

    if (state.isExistingBudgetsDialogVisible) {
        ExistingBudgetsDialog(
            config = ExistingBudgetsDialogConfig(
                items = state.existingBudgets,
                currencyType = state.currencyType,
                onBudgetClick = onExistingBudgetClick,
                onContinue = onContinueCreatingBudget,
                onCancel = onCancelExistingBudgetsDialog
            )
        )
    }
}