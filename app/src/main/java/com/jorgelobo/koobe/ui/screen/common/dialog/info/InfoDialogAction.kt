package com.jorgelobo.koobe.ui.screen.common.dialog.info

sealed interface InfoDialogAction {
    object Open : InfoDialogAction
    object Dismiss : InfoDialogAction
}