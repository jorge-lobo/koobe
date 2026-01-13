package com.jorgelobo.koobe.ui.screen.common.dialog

sealed interface ConfirmationDialogEffect {
    object Confirmed : ConfirmationDialogEffect
}

fun reduceConfirmationDialog(
    state: ConfirmationDialogState,
    action: ConfirmationDialogAction
): Pair<ConfirmationDialogState, ConfirmationDialogEffect?> =
    when (action) {
        ConfirmationDialogAction.RequestClose -> state.copy(visible = true) to null
        ConfirmationDialogAction.Dismiss -> state.copy(visible = false) to null
        ConfirmationDialogAction.Confirm -> state.copy(visible = false) to ConfirmationDialogEffect.Confirmed
    }