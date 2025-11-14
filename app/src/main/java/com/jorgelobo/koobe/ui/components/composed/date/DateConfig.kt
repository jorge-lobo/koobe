package com.jorgelobo.koobe.ui.components.composed.date

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import java.util.Date

data class DateDisplayConfig(
    val periodType: PeriodType,
    val date: Date
)

data class DateSelectorConfig(
    val periodType: PeriodType,
    val date: Date,
    val onLeftClick: () -> Unit,
    val onRightClick: () -> Unit,
    val onPickerClick: () -> Unit
)