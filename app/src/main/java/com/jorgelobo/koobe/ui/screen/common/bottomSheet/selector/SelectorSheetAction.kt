package com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector

sealed interface SelectorSheetAction<out T> {

    object Open : SelectorSheetAction<Nothing>
    object Dismiss : SelectorSheetAction<Nothing>

    data class Select<T>(
        val item: T
    ) : SelectorSheetAction<T>
}