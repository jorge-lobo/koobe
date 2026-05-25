package com.jorgelobo.koobe.ui.screen.common.dialog.selector

fun <T> handleSelectorDialog(
    current: SelectorDialogState<T>,
    action: SelectorDialogAction<T>,
    updateState: (SelectorDialogState<T>) -> Unit,
    onApplied: (T) -> Unit
) {
    val (newState, effect) = reduceSelectorDialog(current, action)
    updateState(newState)

    (effect as? SelectorDialogEffect.Applied)?.let {
        onApplied(it.value)
    }
}