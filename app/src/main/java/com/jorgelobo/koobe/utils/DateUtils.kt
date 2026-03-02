package com.jorgelobo.koobe.utils

import android.annotation.SuppressLint
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
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

    fun getAllMonthsShortNames(): List<String> {
        return (0..11).map { monthIndex ->
            getMonthShortName(monthIndex)
        }
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
            DateFormat.DAY_MONTH -> "dd MMM"
            DateFormat.DAY_MONTH_YEAR -> "dd MMMM yyyy"
            DateFormat.COMPLETE -> "EEEE, dd MMMM yyyy"
        }

        val outputFormat = SimpleDateFormat(pattern, locale)
        return outputFormat.format(date)
    }

    fun Date.formatAs(dateFormat: DateFormat): String =
        formatDate(this, dateFormat)

    fun getDailyItems(date: Date): List<String> =
        withCalendar(date) {
            val days = getActualMaximum(Calendar.DAY_OF_MONTH)
            (1..days).map { it.toString() }
        }

    fun getWeeklyItems(date: Date): List<String> =
        withCalendar(date) {
            val weeks = getActualMaximum(Calendar.WEEK_OF_YEAR)
            (1..weeks).map { "W$it" }
        }

    fun getYearlyItems(
        date: Date,
        range: Int = 20
    ): List<String> =
        withCalendar(date) {
            val year = get(Calendar.YEAR)
            (year - range..year + range).map { it.toString() }
        }

    fun getDailyIndex(date: Date): Int = withCalendar(date) { get(Calendar.DAY_OF_MONTH) - 1 }
    fun getWeeklyIndex(date: Date): Int = withCalendar(date) { get(Calendar.WEEK_OF_YEAR) - 1 }
    fun getMonthlyIndex(date: Date): Int = withCalendar(date) { get(Calendar.MONTH) }
    fun getYearlyIndex(date: Date): Int = withCalendar(date) { get(Calendar.YEAR) - 2000 }

    fun navigate(
        date: Date,
        type: PeriodType,
        direction: Int
    ): Date =
        adjustDate(
            date, when (type) {
                PeriodType.DAILY -> Calendar.DAY_OF_MONTH
                PeriodType.WEEKLY -> Calendar.WEEK_OF_YEAR
                PeriodType.MONTHLY -> Calendar.MONTH
                PeriodType.YEARLY -> Calendar.YEAR
            }, direction
        )

    fun dateFromIndex(
        index: Int,
        type: PeriodType,
        baseDate: Date
    ): Date =
        withCalendar(baseDate) {
            val baseYear = 2000

            when (type) {
                PeriodType.DAILY -> set(Calendar.DAY_OF_MONTH, index + 1)
                PeriodType.WEEKLY -> set(Calendar.WEEK_OF_YEAR, index + 1)
                PeriodType.MONTHLY -> set(Calendar.MONTH, index)
                PeriodType.YEARLY -> set(Calendar.YEAR, baseYear + index)
            }
            time
        }

    fun adjustDate(
        date: Date,
        field: Int,
        amount: Int
    ): Date =
        withCalendar(date) {
            add(field, amount)
            time
        }

    private inline fun <T> withCalendar(
        date: Date,
        calendar: Calendar.() -> T
    ): T {
        val cal = Calendar.getInstance().apply { time = date }
        return cal.calendar()
    }
}