package com.jorgelobo.koobe.ui.components.base.inputs.fields

import com.jorgelobo.koobe.ui.components.model.enums.DateFormat
import com.jorgelobo.koobe.utils.date.DateFormatter
import com.jorgelobo.koobe.utils.date.DateUtils
import java.util.Date

data class InputDateConfig(
    val date: Date = DateUtils.currentDate,
    val onIconClick: () -> Unit
) {
    val dateLabel: String
        get() = DateFormatter.formatDate(date, DateFormat.COMPLETE)
}