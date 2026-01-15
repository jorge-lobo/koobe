package com.jorgelobo.koobe.ui.screen.common.dialog.selector

sealed interface SelectorDialogEffect<T> {
    data class Applied<T>(val value: T) : SelectorDialogEffect<T>
}