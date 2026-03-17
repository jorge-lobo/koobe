package com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.utils.date.DateNavigationUtils
import com.jorgelobo.koobe.utils.date.DateUtils
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
                date = DateUtils.getDailyDate(
                    action.index,
                    state.tempSelectedDate
                )
            )
        }

        is PeriodFilterAction.SelectWeekly -> {
            state.updateTemp(
                date = DateUtils.getWeeklyDate(
                    action.index,
                    state.tempSelectedDate,
                    state.startOfWeek
                )
            )
        }

        is PeriodFilterAction.SelectMonthly -> {
            state.updateTemp(
                date = DateUtils.getMonthlyDate(
                    action.index,
                    state.tempSelectedDate
                )
            )
        }

        is PeriodFilterAction.SelectYearly -> {
            state.updateTemp(
                date = DateUtils.getYearlyDate(
                    action.index,
                    state.currentDate
                )
            )
        }

        PeriodFilterAction.NavigateLeft -> {
            state.updateTemp(
                date = DateNavigationUtils.navigate(
                    state.tempSelectedDate,
                    state.tempSelectedType,
                    -1
                )
            )
        }

        PeriodFilterAction.NavigateRight -> {
            state.updateTemp(
                date = DateNavigationUtils.navigate(
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