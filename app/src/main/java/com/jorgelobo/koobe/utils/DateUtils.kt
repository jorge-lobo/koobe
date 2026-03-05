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

    // ─────────────────────────────
    // Daily utilities
    // ─────────────────────────────

    fun getDailyItems(date: Date): List<String> =
        withCalendar(date) {
            val days = getActualMaximum(Calendar.DAY_OF_MONTH)
            (1..days).map { it.toString() }
        }

    fun getDailyIndex(date: Date): Int =
        withCalendar(date) { get(Calendar.DAY_OF_MONTH) - 1 }

    fun getDailyDate(index: Int, baseDate: Date): Date =
        withCalendar(baseDate) {
            set(Calendar.DAY_OF_MONTH, index + 1)
            time
        }

    // ─────────────────────────────
    // Weekly utilities
    // ─────────────────────────────

    fun getWeeklyItems(date: Date): List<String> =
        withCalendar(date) {
            val year = get(Calendar.YEAR)
            val weeks = getActualMaximum(Calendar.WEEK_OF_YEAR)

            (1..weeks).map { weekNumber ->
                val weekCal = Calendar.getInstance(locale).apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.WEEK_OF_YEAR, weekNumber)
                    set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                }

                val startDate = weekCal.time
                val startDay = SimpleDateFormat("dd", locale).format(startDate)
                val startMonth = SimpleDateFormat("MMM", locale).format(startDate)

                weekCal.add(Calendar.DAY_OF_MONTH, 6)
                val endDate = weekCal.time
                val endDay = SimpleDateFormat("dd", locale).format(endDate)
                val endMonth = SimpleDateFormat("MMM", locale).format(endDate)

                if (startMonth == endMonth) {
                    "$startDay - $endDay $endMonth"
                } else {
                    "$startDay $startMonth - $endDay $endMonth"
                }
            }
        }

    fun getWeeklyIndex(date: Date): Int =
        withCalendar(date) { get(Calendar.WEEK_OF_YEAR) - 1 }

    fun getWeeklyDate(index: Int, baseDate: Date): Date =
        withCalendar(baseDate) {
            set(Calendar.WEEK_OF_YEAR, index + 1)
            set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
            time
        }

    // ─────────────────────────────
    // Monthly utilities
    // ────────────────────────

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

    fun getMonthlyIndex(date: Date): Int =
        withCalendar(date) { get(Calendar.MONTH) }

    fun getMonthlyDate(index: Int, baseDate: Date): Date =
        withCalendar(baseDate) {
            set(Calendar.MONTH, index)
            set(Calendar.DAY_OF_MONTH, 1)
            time
        }

    // ─────────────────────────────
    // Yearly utilities
    // ────────────────────────

    fun getYearlyItems(
        date: Date,
        range: Int = 20
    ): List<String> =
        withCalendar(date) {
            val year = get(Calendar.YEAR)
            (year - range..year).map { it.toString() }
        }

    fun getYearlyIndex(
        date: Date,
        baseDate: Date = currentDate,
        range: Int = 20
    ): Int {
        val targetYear = withCalendar(date) { get(Calendar.YEAR) }
        val baseYear = withCalendar(baseDate) { get(Calendar.YEAR) }
        val index = targetYear - (baseYear - range)
        return index.coerceIn(0, range)
    }

    fun getYearlyDate(index: Int, baseDate: Date, range: Int = 20): Date =
        withCalendar(baseDate) {
            val currentYear = withCalendar(currentDate) { get(Calendar.YEAR) }
            val yearAtIndex = currentYear - range + index
            set(Calendar.YEAR, yearAtIndex)
            set(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.MONTH, Calendar.JANUARY)
            time
        }

    // ─────────────────────────────
    // Navigation
    // ────────────────────────

    fun navigate(
        date: Date,
        type: PeriodType,
        direction: Int
    ): Date =
        adjustDate(
            date, when (type) {
                PeriodType.DAILY -> Calendar.DAY_OF_MONTH
                PeriodType.WEEKLY -> Calendar.YEAR
                PeriodType.MONTHLY -> Calendar.YEAR
                PeriodType.YEARLY -> Calendar.YEAR
            }, direction
        )

    fun adjustDate(
        date: Date,
        field: Int,
        amount: Int
    ): Date =
        withCalendar(date) {
            add(field, amount)
            time
        }

    // ─────────────────────────────
    // Helpers
    // ────────────────────────

    fun isMonthInFuture(monthIndex: Int, referenceDate: Date): Boolean {
        val now = Calendar.getInstance().apply { time = currentDate }
        val reference = Calendar.getInstance().apply { time = referenceDate }

        val currentYear = now.get(Calendar.YEAR)
        val currentMonth = now.get(Calendar.MONTH)
        val referenceYear = reference.get(Calendar.YEAR)

        if (referenceYear == currentYear) {
            return monthIndex > currentMonth
        }

        return referenceYear > currentYear
    }

    fun isWeekInFuture(weekIndex: Int, referenceDate: Date): Boolean {
        val now = Calendar.getInstance().apply { time = currentDate }
        val reference = Calendar.getInstance().apply { time = referenceDate }

        val currentYear = now.get(Calendar.YEAR)
        val currentWeek = now.get(Calendar.WEEK_OF_YEAR)
        val referenceYear = reference.get(Calendar.YEAR)

        if (referenceYear == currentYear) {
            return weekIndex > currentWeek
        }

        return referenceYear > currentYear
    }

    fun isDayInFuture(dayIndex: Int, referenceDate: Date): Boolean {
        val now = Calendar.getInstance().apply { time = currentDate }
        val reference = Calendar.getInstance().apply { time = referenceDate }

        val currentYear = now.get(Calendar.YEAR)
        val currentMonth = now.get(Calendar.MONTH)
        val currentDay = now.get(Calendar.DAY_OF_MONTH)
        val referenceYear = reference.get(Calendar.YEAR)
        val referenceMonth = reference.get(Calendar.MONTH)

        if (referenceYear != currentYear) {
            return referenceYear > currentYear
        }

        if (referenceMonth != currentMonth) {
            return referenceMonth > currentMonth
        }

        return dayIndex > (currentDay - 1)
    }

    private inline fun <T> withCalendar(
        date: Date,
        calendar: Calendar.() -> T
    ): T {
        val cal = Calendar.getInstance().apply { time = date }
        return cal.calendar()
    }
}