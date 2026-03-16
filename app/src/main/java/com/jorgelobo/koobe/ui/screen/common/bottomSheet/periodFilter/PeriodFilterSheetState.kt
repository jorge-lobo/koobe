package com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.utils.date.DateUtils
import java.util.Date

data class PeriodFilterSheetState(
    val visible: Boolean = false,
    val startOfWeek: StartOfWeek = StartOfWeek.SUNDAY,
    val selectedType: PeriodType = PeriodType.MONTHLY,
    val selectedDate: Date = DateUtils.currentDate,
    val tempSelectedType: PeriodType = PeriodType.MONTHLY,
    val tempSelectedDate: Date = DateUtils.currentDate
)