package com.jorgelobo.koobe.ui.screen.budgets.editor.state

import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState

/**
 * Internal state for the Budget Editor screen, managing the visibility of confirmation dialogs
 * and the execution status of asynchronous operations like saving or deleting.
 *
 * @property discardDialog State for the dialog shown when there are unsaved changes.
 * @property deleteDialog State for the dialog shown when confirming budget deletion.
 * @property duplicateBudgetDialog State for the dialog shown when a budget with a similar name already exists.
 * @property isSaving Indicates if a save operation is currently in progress.
 * @property isDeleting Indicates if a delete operation is currently in progress.
 * @property hasTriedToSave Indicates if the user has attempted to submit the form, used for triggering validation error visibility.
 * @property existingBudgetsDialogDismissedFor Stores the identifier of a budget for which the duplication warning has been acknowledged or dismissed.
 */
data class BudgetUiStateInternal(
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val duplicateBudgetDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val hasTriedToSave: Boolean = false,
    val existingBudgetsDialogDismissedFor: Int? = null
)