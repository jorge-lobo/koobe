package com.jorgelobo.koobe.ui.screen.historic

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.composed.sheets.DateNavigation
import com.jorgelobo.koobe.ui.components.composed.sheets.FilterActions
import com.jorgelobo.koobe.ui.components.composed.sheets.PeriodFilterBottomSheetConfig
import com.jorgelobo.koobe.ui.components.composed.sheets.PeriodListState
import com.jorgelobo.koobe.ui.components.composed.sheets.PeriodSelection
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter.PeriodFilterAction
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter.PeriodFilterSheetState
import com.jorgelobo.koobe.utils.date.DateUtils
import com.jorgelobo.koobe.utils.date.PeriodUtils

fun buildPeriodFilterConfig(
    filter: PeriodFilterSheetState,
    onAction: (PeriodFilterAction) -> Unit
): PeriodFilterBottomSheetConfig {

    val currentDate = DateUtils.currentDate
    val tempDate = filter.tempSelectedDate
    val tempType = filter.tempSelectedType

    val items = when (tempType) {
        PeriodType.DAILY -> PeriodUtils.getDailyItems(tempDate)
        PeriodType.WEEKLY -> PeriodUtils.getWeeklyItems(tempDate)
        PeriodType.MONTHLY -> PeriodUtils.getAllMonthsShortNames()
        PeriodType.YEARLY -> PeriodUtils.getYearlyItems(currentDate)
    }

    val selectedIndex = when (tempType) {
        PeriodType.DAILY -> DateUtils.getDailyIndex(tempDate)
        PeriodType.WEEKLY -> DateUtils.getWeeklyIndex(tempDate)
        PeriodType.MONTHLY -> DateUtils.getMonthlyIndex(tempDate)
        PeriodType.YEARLY -> DateUtils.getYearlyIndex(tempDate, baseDate = currentDate)
    }

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
        periodListState = PeriodListState(
            items = items,
            selectedIndex = selectedIndex,
            periodType = tempType,
            referenceDate = tempDate
        ),
        onPeriodItemSelected = { index ->
            when (tempType) {
                PeriodType.DAILY -> onAction(PeriodFilterAction.SelectDaily(index))
                PeriodType.WEEKLY -> onAction(PeriodFilterAction.SelectWeekly(index))
                PeriodType.MONTHLY -> onAction(PeriodFilterAction.SelectMonthly(index))
                PeriodType.YEARLY -> onAction(PeriodFilterAction.SelectYearly(index))
            }
        },
        actions = FilterActions(
            onOpenDatePicker = { onAction(PeriodFilterAction.OpenDatePicker) },
            onApply = { onAction(PeriodFilterAction.Apply) },
            onCancel = { onAction(PeriodFilterAction.Dismiss) }
        )
    )
}