package com.jorgelobo.koobe.utils

import android.annotation.SuppressLint
import com.jorgelobo.koobe.ui.components.model.enums.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    @SuppressLint("ConstantLocale")
    /*private val locale = Locale.getDefault()*/
    private val locale = Locale.ENGLISH

    val currentDate: Date
        get() = Calendar.getInstance().time

    fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun getMonthShortName(monthIndex: Int): String {
        val calendar = Calendar.getInstance(locale).apply { set(Calendar.MONTH, monthIndex) }
        return SimpleDateFormat("MMM", locale)
            .format(calendar.time)
            .replaceFirstChar { it.uppercaseChar() }
    }

    fun formatDate(date: Date, dateFormat: DateFormat): String {
        val pattern = when (dateFormat) {
            DateFormat.YEAR -> "yyyy"
            DateFormat.MONTH_YEAR -> "MMMM yyyy"
            DateFormat.DAY_MONTH_YEAR -> "dd MMMM yyyy"
            DateFormat.COMPLETE -> "EEEE, dd MMMM yyyy"
        }

        val outputFormat = SimpleDateFormat(pattern, locale)
        return outputFormat.format(date)
    }

    fun Date.formatAs(dateFormat: DateFormat): String =
        formatDate(this, dateFormat)
}