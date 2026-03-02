package com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.utils.DateUtils
import java.util.Date

fun reducePeriodFilter(
    state: PeriodFilterSheetState,
    action: PeriodFilterAction
): PeriodFilterSheetState {
    return when (action) {

        PeriodFilterAction.Open ->
            state.copy(
                visible = true,
                tempSelectedType = state.selectedType,
                tempSelectedDate = state.selectedDate
            )

        PeriodFilterAction.Dismiss ->
            state.copy(visible = false)

        PeriodFilterAction.Apply ->
            state.copy(
                visible = false,
                selectedType = state.tempSelectedType,
                selectedDate = state.tempSelectedDate
            )

        is PeriodFilterAction.SelectType ->
            state.copy(tempSelectedType = action.type)

        is PeriodFilterAction.SelectDate ->
            state.copy(tempSelectedDate = action.date)

        is PeriodFilterAction.SelectDaily -> {
            state.updateTemp(
                date = DateUtils.dateFromIndex(
                    action.index,
                    PeriodType.DAILY,
                    state.tempSelectedDate
                )
            )
        }

        is PeriodFilterAction.SelectWeekly -> {
            state.updateTemp(
                date = DateUtils.dateFromIndex(
                    action.index,
                    PeriodType.WEEKLY,
                    state.tempSelectedDate
                )
            )
        }

        is PeriodFilterAction.SelectMonthly -> {
            state.updateTemp(
                date = DateUtils.dateFromIndex(
                    action.index,
                    PeriodType.MONTHLY,
                    state.tempSelectedDate
                )
            )
        }

        is PeriodFilterAction.SelectYearly -> {
            state.updateTemp(
                date = DateUtils.dateFromIndex(
                    action.index,
                    PeriodType.YEARLY,
                    state.tempSelectedDate
                )
            )
        }

        PeriodFilterAction.NavigateLeft -> {
            state.updateTemp(
                date = DateUtils.navigate(
                    state.tempSelectedDate,
                    state.tempSelectedType,
                    -1
                )
            )
        }

        PeriodFilterAction.NavigateRight -> {
            state.updateTemp(
                date = DateUtils.navigate(
                    state.tempSelectedDate,
                    state.tempSelectedType,
                    1
                )
            )
        }

        PeriodFilterAction.OpenDatePicker ->
            state
    }
}

private fun PeriodFilterSheetState.updateTemp(
    type: PeriodType = tempSelectedType,
    date: Date = tempSelectedDate
) = copy(
    tempSelectedType = type,
    tempSelectedDate = date
)