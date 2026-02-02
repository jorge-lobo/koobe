package com.jorgelobo.koobe.ui.screen.common.dialog.datePicker

import java.util.Date

sealed interface DatePickerDialogAction {
    object Open : DatePickerDialogAction
    object Dismiss : DatePickerDialogAction
    data class Select(val date: Date) : DatePickerDialogAction
    object Confirm : DatePickerDialogAction
}