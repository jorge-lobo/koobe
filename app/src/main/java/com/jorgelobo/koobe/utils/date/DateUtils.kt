package com.jorgelobo.koobe.utils.date

import android.annotation.SuppressLint
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    @SuppressLint("ConstantLocale")
            /*private val locale = Locale.getDefault()*/
    val locale: Locale = Locale.ENGLISH

    val currentDate: Date
        get() = DateProvider.now

    // ─────────────────────────────
    // Core helper
    // ─────────────────────────────

    inline fun <T> withCalendar(
        date: Date,
        block: Calendar.() -> T
    ): T {
        val cal = Calendar.getInstance().apply { time = date }
        return cal.block()
    }

    // ─────────────────────────────
    // Generic helpers
    // ─────────────────────────────

    fun Date.get(field: Int): Int =
        withCalendar(this) { get(field) }

    fun Date.modify(block: Calendar.() -> Unit): Date =
        withCalendar(this) {
            block()
            time
        }

    // ─────────────────────────────
    // Comparisons
    // ─────────────────────────────

    fun isSameDay(date1: Date, date2: Date): Boolean =
        date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
                date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR)

    fun isSameMonth(date1: Date, date2: Date): Boolean =
        date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
                date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)

    fun isSameYear(date1: Date, date2: Date): Boolean =
        date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)

    // ─────────────────────────────
    // Daily
    // ─────────────────────────────

    fun getDailyIndex(date: Date): Int =
        date.get(Calendar.DAY_OF_MONTH) - 1

    fun getDailyDate(index: Int, baseDate: Date): Date =
        baseDate.modify {
            set(Calendar.DAY_OF_MONTH, index + 1)
        }

    // ─────────────────────────────
    // Weekly
    // ─────────────────────────────

    fun getWeeklyIndex(date: Date): Int =
        date.weekIndex()

    fun getWeeklyDate(index: Int, baseDate: Date): Date =
        baseDate.modify {
            set(Calendar.WEEK_OF_YEAR, index + 1)
            set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        }

    // ─────────────────────────────
    // Monthly
    // ────────────────────────

    fun getMonthlyIndex(date: Date): Int =
        date.get(Calendar.MONTH)

    fun getMonthlyDate(index: Int, baseDate: Date): Date =
        baseDate.modify {
            set(Calendar.MONTH, index)
            set(Calendar.DAY_OF_MONTH, 1)
        }

    // ─────────────────────────────
    // Yearly
    // ────────────────────────

    fun getYearlyIndex(
        date: Date,
        baseDate: Date = currentDate,
        range: Int = 20
    ): Int {

        val targetYear = date.get(Calendar.YEAR)
        val baseYear = baseDate.get(Calendar.YEAR)

        val index = targetYear - (baseYear - range)

        return index.coerceIn(0, range)
    }

    fun getYearlyDate(
        index: Int,
        baseDate: Date,
        range: Int = 20
    ): Date {
        val currentYear = currentDate.get(Calendar.YEAR)

        return baseDate.modify{
            val year = currentYear - range + index
            set(Calendar.YEAR, year)
            set(Calendar.DAY_OF_YEAR, 1)
        }
    }
}