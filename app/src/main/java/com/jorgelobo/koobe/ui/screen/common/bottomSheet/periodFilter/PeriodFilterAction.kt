package com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import java.util.Date

sealed interface PeriodFilterAction {

    object Open : PeriodFilterAction
    object Dismiss : PeriodFilterAction
    object Apply : PeriodFilterAction
    object NavigateLeft : PeriodFilterAction
    object NavigateRight : PeriodFilterAction
    object OpenDatePicker : PeriodFilterAction

    data class SelectType(val type: PeriodType) : PeriodFilterAction
    data class SelectDate(val date: Date) : PeriodFilterAction
    data class SelectDaily(val index: Int) : PeriodFilterAction
    data class SelectWeekly(val index: Int) : PeriodFilterAction
    data class SelectMonthly(val index: Int) : PeriodFilterAction
    data class SelectYearly(val index: Int) : PeriodFilterAction
}