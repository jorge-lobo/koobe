package com.jorgelobo.koobe.ui.screen.common.dialog.confirmation

fun handleConfirmationDialog(
    current: ConfirmationDialogState,
    action: ConfirmationDialogAction,
    updateState: (ConfirmationDialogState) -> Unit,
    onConfirmed: () -> Unit
) {
    val (newState, effect) = reduceConfirmationDialog(current, action)
    updateState(newState)

    if (effect is ConfirmationDialogEffect.Confirmed) {
        onConfirmed()
    }
}