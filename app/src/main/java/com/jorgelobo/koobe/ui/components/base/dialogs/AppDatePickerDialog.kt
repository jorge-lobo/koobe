package com.jorgelobo.koobe.ui.components.base.dialogs

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.ui.theme.AppTheme
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(
    config: AppDatePickerDialogConfig
) {
    if (!config.visible) return

    val colors = AppTheme.colors

    val pickerState = rememberDatePickerState(initialSelectedDateMillis = config.selectedDate.time)

    DatePickerDialog(
        onDismissRequest = config.onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    pickerState.selectedDateMillis?.let {
                        config.onDateSelected(Date(it))
                        config.onConfirm()
                    }
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colors.buttonColors.buttonTextDefault
                )
            ) {
                Text(stringResource(config.confirmTextRes))
            }
        },
        dismissButton = {
            TextButton(
                onClick = config.onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colors.buttonColors.buttonTextDefault
                )
            ) {
                Text(stringResource(config.dismissTextRes))
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = colors.containerColors.containerPrimary
        )
    ) {
        DatePicker(
            state = pickerState,
            colors = DatePickerDefaults.colors(
                containerColor = colors.containerColors.containerPrimary,
                titleContentColor = colors.textColors.textPrimary,
                headlineContentColor = colors.textColors.textPrimary,
                subheadContentColor = colors.textColors.textPrimary,
                navigationContentColor = colors.textColors.textPrimary,
                selectedDayContainerColor = colors.containerColors.containerSelected,
                selectedDayContentColor = colors.iconColors.iconSelected,
                selectedYearContainerColor = colors.containerColors.containerSelected,
                selectedYearContentColor = colors.iconColors.iconSelected,
                todayDateBorderColor = colors.buttonColors.buttonTextDefault,
                dateTextFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.containerColors.containerSelected,
                    unfocusedBorderColor = colors.containerColors.containerOutline,
                    errorBorderColor = colors.negative,
                    errorSupportingTextColor = colors.negative,
                    errorLabelColor = colors.negative,
                    errorCursorColor = colors.negative,
                    errorTextColor = colors.negative
                )
            ),
            showModeToggle = true
        )
    }
}