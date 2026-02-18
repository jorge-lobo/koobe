package com.jorgelobo.koobe.ui.screen.common.dialog.datePicker

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import java.util.Date

@Stable
data class DatePickerDialogState(
    val visible: Boolean = false,
    val selectedDate: Date,
    val language: AppLanguage
)