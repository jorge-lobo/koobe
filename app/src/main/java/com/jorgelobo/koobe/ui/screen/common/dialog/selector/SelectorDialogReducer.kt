package com.jorgelobo.koobe.ui.screen.common.dialog.selector

fun <T> reduceSelectorDialog(
    state: SelectorDialogState<T>,
    action: SelectorDialogAction<T>
): Pair<SelectorDialogState<T>, SelectorDialogEffect<T>?> =
    when (action) {

        is SelectorDialogAction.Open ->
            state.copy(visible = true, selected = state.initial) to null

        is SelectorDialogAction.Select ->
            state.copy(selected = action.item) to null

        is SelectorDialogAction.Cancel ->
            state.copy(visible = false, selected = state.initial) to null

        is SelectorDialogAction.Apply ->
            if (state.selected != null)
                state.copy(
                    visible = false,
                    initial = state.selected
                ) to SelectorDialogEffect.Applied(state.selected)
            else state to null
    }