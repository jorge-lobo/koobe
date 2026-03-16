package com.jorgelobo.koobe.utils.date

import android.util.Log
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.utils.date.DateUtils.clearTime
import java.util.Date

object DateFutureUtils {

    private fun today(): Date = DateUtils.currentDate.clearTime()

    fun isMonthInFuture(monthIndex: Int, referenceDate: Date): Boolean {
        val itemDate = DateUtils.getMonthlyDate(monthIndex, referenceDate)
        return itemDate.after(today())
    }

    fun isWeekInFuture(weekIndex: Int, referenceDate: Date, startOfWeek: StartOfWeek): Boolean {
        val itemStart = DateUtils.getWeeklyDate(weekIndex, referenceDate, startOfWeek)
        val today = today()

        Log.d(
            "DateFutureUtils",
            "isWeekInFuture: itemStart = $itemStart, today = $today, startOfWeek = $startOfWeek"
        )
        return itemStart.after(today())
    }

    fun isDayInFuture(dayIndex: Int, referenceDate: Date): Boolean {
        val itemDate = DateUtils.getDailyDate(dayIndex, referenceDate)
        return itemDate.after(today())
    }
}