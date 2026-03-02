package com.jorgelobo.koobe.ui.screen.historic

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.ui.components.composed.sheets.PeriodFilterBottomSheet
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter.PeriodFilterAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricDialogs(
    state: HistoricUiState,
    sheetState: SheetState,
    onPeriodFilterAction: (PeriodFilterAction) -> Unit
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
}