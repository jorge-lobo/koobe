package com.jorgelobo.koobe.utils.date

import com.jorgelobo.koobe.utils.date.DateUtils.locale
import com.jorgelobo.koobe.utils.date.DateUtils.withCalendar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object PeriodUtils {

    fun getDailyItems(date: Date): List<String> =
        withCalendar(date) {
            val days = getActualMaximum(Calendar.DAY_OF_MONTH)
            (1..days).map { it.toString() }
        }

    fun getWeeklyItems(
        date: Date,
        startOfWeek: Int
    ): List<String> =
        withCalendar(date) {
            val year = get(Calendar.YEAR)
            val weeks = getActualMaximum(Calendar.WEEK_OF_YEAR)

            (1..weeks).map { weekNumber ->
                val weekCal = Calendar.getInstance(locale).apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.WEEK_OF_YEAR, weekNumber)
                    set(Calendar.DAY_OF_WEEK, startOfWeek)
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

    fun getYearlyItems(
        date: Date,
        range: Int = 20
    ): List<String> =
        withCalendar(date) {
            val year = get(Calendar.YEAR)
            (year - range..year).map { it.toString() }
        }
}