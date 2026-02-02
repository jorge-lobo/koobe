package com.jorgelobo.koobe.ui.screen.common.dialog.selector

data class SelectorDialogState<T>(
    val visible: Boolean = false,
    val selected: T? = null,
    val initial: T? = null
) {
    val hasChanges: Boolean
        get() = selected != initial
}