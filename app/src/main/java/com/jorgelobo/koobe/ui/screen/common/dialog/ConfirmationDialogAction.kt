package com.jorgelobo.koobe.ui.screen.common.dialog

sealed interface ConfirmationDialogAction {
    object RequestClose : ConfirmationDialogAction
    object Dismiss : ConfirmationDialogAction
    object Confirm : ConfirmationDialogAction
}