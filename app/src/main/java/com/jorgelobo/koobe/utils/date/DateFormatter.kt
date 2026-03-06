package com.jorgelobo.koobe.utils.date

import com.jorgelobo.koobe.ui.components.model.enums.DateFormat
import com.jorgelobo.koobe.utils.date.DateUtils
import java.text.SimpleDateFormat
import java.util.Date

object DateFormatter {

    fun formatDate(date: Date, dateFormat: DateFormat): String {
        val pattern = when (dateFormat) {
            DateFormat.YEAR -> "yyyy"
            DateFormat.MONTH_YEAR -> "MMMM yyyy"
            DateFormat.DAY_MONTH -> "dd MMM"
            DateFormat.DAY_MONTH_YEAR -> "dd MMMM yyyy"
            DateFormat.COMPLETE -> "EEEE, dd MMMM yyyy"
        }

        return SimpleDateFormat(pattern, DateUtils.locale).format(date)
    }

    fun Date.formatAs(dateFormat: DateFormat): String =
        DateFormatter.formatDate(this, dateFormat)
}
