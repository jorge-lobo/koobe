package com.jorgelobo.koobe.ui.screen.historic

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.ui.components.base.dialogs.AppDatePickerDialog
import com.jorgelobo.koobe.ui.components.base.dialogs.AppDatePickerDialogConfig
import com.jorgelobo.koobe.ui.components.composed.sheets.PeriodFilterBottomSheet
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter.PeriodFilterAction
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricDialogs(
    state: HistoricUiState,
    sheetState: SheetState,
    onPeriodFilterAction: (PeriodFilterAction) -> Unit,
    onDatePickerDialogAction: (DatePickerDialogAction) -> Unit
) {

    if (state.periodFilter.visible) {
        PeriodFilterBottomSheet(
            sheetState = sheetState,
            config = buildPeriodFilterConfig(
                filter = state.periodFilter,
                onAction = onPeriodFilterAction
            ),
            onDismiss = { onPeriodFilterAction(PeriodFilterAction.Dismiss) }
        )
    }

    if (state.datePickerDialog.visible) {
        AppDatePickerDialog(
            config = AppDatePickerDialogConfig(
                visible = state.datePickerDialog.visible,
                language = state.datePickerDialog.language,
                selectedDate = state.datePickerDialog.selectedDate,
                onDateSelected = { onDatePickerDialogAction(DatePickerDialogAction.Select(it)) },
                onConfirm = { onDatePickerDialogAction(DatePickerDialogAction.Confirm) },
                onDismiss = { onDatePickerDialogAction(DatePickerDialogAction.Dismiss) }
            )
        )
    }
}