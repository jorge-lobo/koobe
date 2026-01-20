package com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector

data class SelectorSheetState<T>(
    val visible: Boolean = false,
    val selected: T
)