package com.jorgelobo.koobe.ui.screen.historic

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.composed.sheets.DateNavigation
import com.jorgelobo.koobe.ui.components.composed.sheets.FilterActions
import com.jorgelobo.koobe.ui.components.composed.sheets.PeriodConfig
import com.jorgelobo.koobe.ui.components.composed.sheets.PeriodFilterBottomSheetConfig
import com.jorgelobo.koobe.ui.components.composed.sheets.PeriodSelection
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter.PeriodFilterAction
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter.PeriodFilterSheetState
import com.jorgelobo.koobe.utils.DateUtils

fun buildPeriodFilterConfig(
    filter: PeriodFilterSheetState,
    onAction: (PeriodFilterAction) -> Unit
): PeriodFilterBottomSheetConfig {

    val currentDate = DateUtils.currentDate
    val tempDate = filter.tempSelectedDate
    val tempType = filter.tempSelectedType

    val dailyItems = DateUtils.getDailyItems(tempDate)
    val weeklyItems = DateUtils.getWeeklyItems(tempDate)
    val monthlyItems = DateUtils.getAllMonthsShortNames()
    val yearlyItems = DateUtils.getYearlyItems(currentDate)

    return PeriodFilterBottomSheetConfig(
        selected = PeriodSelection(
            type = tempType,
            date = tempDate
        ),
        onSelectionChanged = { selection ->
            onAction(PeriodFilterAction.SelectType(selection.type))
        },
        dateNavigation = DateNavigation(
            onLeftClick = { onAction(PeriodFilterAction.NavigateLeft) },
            onRightClick = { onAction(PeriodFilterAction.NavigateRight) },
            onPickerClick = { onAction(PeriodFilterAction.OpenDatePicker) }
        ),
        periodConfig = when (tempType) {
            PeriodType.DAILY -> PeriodConfig.Daily(
                items = dailyItems,
                selectedIndex = DateUtils.getDailyIndex(tempDate),
                onItemSelected = { index -> onAction(PeriodFilterAction.SelectDaily(index)) }
            )

            PeriodType.WEEKLY -> PeriodConfig.Weekly(
                items = weeklyItems,
                selectedIndex = DateUtils.getWeeklyIndex(tempDate),
                onItemSelected = { index -> onAction(PeriodFilterAction.SelectWeekly(index)) }
            )

            PeriodType.MONTHLY -> PeriodConfig.Monthly(
                items = monthlyItems,
                selectedIndex = DateUtils.getMonthlyIndex(tempDate),
                onItemSelected = { index -> onAction(PeriodFilterAction.SelectMonthly(index)) }
            )

            PeriodType.YEARLY -> PeriodConfig.Yearly(
                items = yearlyItems,
                selectedIndex = DateUtils.getYearlyIndex(tempDate, baseDate = currentDate),
                onItemSelected = { index -> onAction(PeriodFilterAction.SelectYearly(index)) }
            )
        },
        actions = FilterActions(
            onApply = { onAction(PeriodFilterAction.Apply) },
            onCancel = { onAction(PeriodFilterAction.Dismiss) }
        )
    )
}