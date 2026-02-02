package com.jorgelobo.koobe.ui.components.base.dialogs

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.R
import java.util.Date

@Stable
data class AppDatePickerDialogConfig(
    val visible: Boolean,
    val selectedDate: Date,
    val onDateSelected: (Date) -> Unit,
    val onConfirm: () -> Unit,
    val onDismiss: () -> Unit,
    val confirmTextRes: Int = R.string.btn_apply,
    val dismissTextRes: Int = R.string.btn_cancel
)