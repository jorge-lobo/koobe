package com.jorgelobo.koobe.ui.screen.historic

import com.jorgelobo.koobe.ui.components.composed.sheets.PeriodFilterBottomSheetConfig
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter.PeriodFilterAction
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter.PeriodFilterSheetState
import com.jorgelobo.koobe.utils.DateUtils

fun buildPeriodFilterConfig(
    filter: PeriodFilterSheetState,
    onAction: (PeriodFilterAction) -> Unit
): PeriodFilterBottomSheetConfig {

    val tempDate = filter.tempSelectedDate
    val tempType = filter.tempSelectedType

    val dailyItems = DateUtils.getDailyItems(tempDate)
    val weeklyItems = DateUtils.getWeeklyItems(tempDate)
    val yearlyItems = DateUtils.getYearlyItems(tempDate)

    return PeriodFilterBottomSheetConfig(
        type = filter.selectedType,
        selectedType = tempType,
        onTypeSelected = { onAction(PeriodFilterAction.SelectType(it)) },
        date = filter.selectedDate,
        selectedDate = tempDate,
        onDateSelected = { onAction(PeriodFilterAction.SelectDate(it)) },
        onLeftClick = { onAction(PeriodFilterAction.NavigateLeft) },
        onRightClick = { onAction(PeriodFilterAction.NavigateRight) },
        onPickerClick = { onAction(PeriodFilterAction.OpenDatePicker) },
        dailyItems = dailyItems,
        weeklyItems = weeklyItems,
        yearlyItems = yearlyItems,
        selectedDailyIndex = DateUtils.getDailyIndex(tempDate),
        selectedWeeklyIndex = DateUtils.getWeeklyIndex(tempDate),
        selectedMonthlyIndex = DateUtils.getMonthlyIndex(tempDate),
        selectedYearlyIndex = DateUtils.getYearlyIndex(tempDate),
        onDailySelected = { onAction(PeriodFilterAction.SelectDaily(it)) },
        onWeeklySelected = { onAction(PeriodFilterAction.SelectWeekly(it)) },
        onMonthlySelected = { onAction(PeriodFilterAction.SelectMonthly(it)) },
        onYearlySelected = { onAction(PeriodFilterAction.SelectYearly(it)) },
        onApply = { onAction(PeriodFilterAction.Apply) },
        onCancel = { onAction(PeriodFilterAction.Dismiss) }
    )
}