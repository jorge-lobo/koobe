package com.jorgelobo.koobe.ui.screen.common.dialog.info

sealed interface InfoDialogEffect {
    object Dismiss : InfoDialogEffect
}

fun reduceInfoDialog(
    state: InfoDialogState,
    action: InfoDialogAction
): Pair<InfoDialogState, InfoDialogEffect?> =
    when (action) {
        InfoDialogAction.Open -> state.copy(visible = true) to null
        InfoDialogAction.Dismiss -> state.copy(visible = false) to null
    }