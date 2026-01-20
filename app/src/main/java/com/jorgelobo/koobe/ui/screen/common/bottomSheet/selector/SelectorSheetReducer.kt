package com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector

fun <T> reduceSelectorSheet(
    state: SelectorSheetState<T>,
    action: SelectorSheetAction<T>
): SelectorSheetState<T> =
    when (action) {

        SelectorSheetAction.Open -> state.copy(visible = true)

        SelectorSheetAction.Dismiss -> state.copy(visible = false)

        is SelectorSheetAction.Select -> state.copy(
            visible = false,
            selected = action.item
        )
    }