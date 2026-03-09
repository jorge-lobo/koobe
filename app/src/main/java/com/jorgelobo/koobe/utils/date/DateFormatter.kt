package com.jorgelobo.koobe.utils.date

import com.jorgelobo.koobe.ui.components.model.enums.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object DateFormatter {

    private val dayMonthFormatter by lazy {
        SimpleDateFormat("dd MMM", DateUtils.locale)
    }

    fun formatDate(date: Date, dateFormat: DateFormat): String {

        if (dateFormat == DateFormat.WEEK) {
            return formatWeek(date)
        }

        val pattern = when (dateFormat) {
            DateFormat.YEAR -> "yyyy"
            DateFormat.MONTH_YEAR -> "MMMM yyyy"
            DateFormat.DAY_MONTH -> "dd MMM"
            DateFormat.DAY_MONTH_YEAR -> "dd MMMM yyyy"
            DateFormat.COMPLETE -> "EEEE, dd MMMM yyyy"
            DateFormat.WEEK -> error("Handled above")
        }

        return SimpleDateFormat(pattern, DateUtils.locale).format(date)
    }

    private fun formatWeek(date: Date): String {
        val start = date.startOfWeek()
        val end = date.endOfWeek()

        return "${dayMonthFormatter.format(start)} - ${dayMonthFormatter.format(end)}"
    }

    fun Date.formatAs(dateFormat: DateFormat): String =
        formatDate(this, dateFormat)

    fun Date.startOfWeek(): Date =
        modify { set(Calendar.DAY_OF_WEEK, firstDayOfWeek) }

    fun Date.endOfWeek(): Date =
        startOfWeek().modify { add(Calendar.DAY_OF_MONTH, 6) }
}