package com.jorgelobo.koobe.ui.screen.common.dialog.selector

sealed interface SelectorDialogAction<out T> {
    object Open : SelectorDialogAction<Nothing>
    object Cancel : SelectorDialogAction<Nothing>
    object Apply : SelectorDialogAction<Nothing>
    data class Select<T>(val item: T) : SelectorDialogAction<T>
}