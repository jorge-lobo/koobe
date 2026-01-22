package com.jorgelobo.koobe.ui.screen.common.dialog.datePicker

fun reduceDatePickerDialog(
    state: DatePickerDialogState,
    action: DatePickerDialogAction
): Pair<DatePickerDialogState, DatePickerDialogEffect?> =
    when (action) {

        DatePickerDialogAction.Open ->
            state.copy(visible = true) to null

        DatePickerDialogAction.Dismiss ->
            state.copy(visible = false) to null

        is DatePickerDialogAction.Select ->
            state.copy(selectedDate = action.date) to null

        DatePickerDialogAction.Confirm ->
            state.copy(visible = false) to DatePickerDialogEffect.Confirmed(state.selectedDate)
    }