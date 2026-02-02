package com.jorgelobo.koobe.ui.screen.common.dialog.datePicker

import java.util.Date

sealed interface DatePickerDialogEffect {
    data class Confirmed(val date: Date) : DatePickerDialogEffect
}