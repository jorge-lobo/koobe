package com.jorgelobo.koobe.utils.date

import java.util.Date

object DateFutureUtils {

    private val currentDate: Date
        get() = DateUtils.currentDate

    fun isIndexInFuture(
        index: Int,
        currentIndex: Int,
        referenceYear: Int,
        currentYear: Int
    ): Boolean {
        return if (referenceYear == currentYear) {
            index > currentIndex
        } else {
            referenceYear > currentYear
        }
    }

    fun isMonthInFuture(monthIndex: Int, referenceDate: Date) =
        isIndexInFuture(
            monthIndex,
            currentDate.month(),
            referenceDate.year(),
            currentDate.year()
        )

    fun isWeekInFuture(weekIndex: Int, referenceDate: Date) =
        isIndexInFuture(
            weekIndex,
            currentDate.weekIndex(),
            referenceDate.year(),
            currentDate.year()
        )

    fun isDayInFuture(dayIndex: Int, referenceDate: Date) =
        isIndexInFuture(
            dayIndex,
            currentDate.day() - 1,
            referenceDate.year(),
            currentDate.year()
        )
}