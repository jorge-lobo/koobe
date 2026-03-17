package com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.utils.date.DateUtils
import java.util.Date

/**
 * Represents the UI state for the Period Filter bottom sheet.
 *
 * This state manages the visibility of the sheet, the user's preferred start of the week, and
 * handles both the currently applied filter settings and the temporary selections made while
 * the bottom sheet is open.
 *
 * @property visible Whether the bottom sheet is currently displayed on the screen.
 * @property startOfWeek The configured first day of the week (e.g., Sunday or Monday).
 * @property currentDate The current system date, used as a reference for "today".
 * @property selectedType The finalized [PeriodType] currently applied to the data filter.
 * @property selectedDate The finalized [Date] currently applied to the data filter.
 */
data class PeriodFilterSheetState(
    val visible: Boolean = false,
    val startOfWeek: StartOfWeek = StartOfWeek.SUNDAY,
    val currentDate: Date = DateUtils.currentDate,
    val selectedType: PeriodType = PeriodType.MONTHLY,
    val selectedDate: Date = DateUtils.currentDate,
    val tempSelectedType: PeriodType = PeriodType.MONTHLY,
    val tempSelectedDate: Date = DateUtils.currentDate
)