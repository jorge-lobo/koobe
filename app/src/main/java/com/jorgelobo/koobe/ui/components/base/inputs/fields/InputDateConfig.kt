package com.jorgelobo.koobe.ui.components.base.inputs.fields

import com.jorgelobo.koobe.utils.DateUtils
import java.util.Date

data class InputDateConfig(
    val date: Date = DateUtils.currentDate,
    val onIconClick: () -> Unit
) {
    val dateLabel: String
        get() = DateUtils.formatDateLong(date)
}