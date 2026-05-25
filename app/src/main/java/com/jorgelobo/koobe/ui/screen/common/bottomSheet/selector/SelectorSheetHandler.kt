package com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector

fun <T> handleSelectorSheet(
    current: SelectorSheetState<T>,
    action: SelectorSheetAction<T>,
    updateState: (SelectorSheetState<T>) -> Unit,
    onApplied: (T) -> Unit
) {
    val newState = reduceSelectorSheet(current, action)
    updateState(newState)

    if (action is SelectorSheetAction.Select) {
        onApplied(action.item)
    }
}