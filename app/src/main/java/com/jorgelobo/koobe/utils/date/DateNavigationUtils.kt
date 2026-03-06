package com.jorgelobo.koobe.utils.date

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import java.util.Calendar
import java.util.Date

object DateNavigationUtils {

    fun navigate(
        date: Date,
        type: PeriodType,
        direction: Int
    ): Date {
        return adjustDate(
            date,
            when (type) {
                PeriodType.DAILY -> Calendar.DAY_OF_MONTH
                PeriodType.WEEKLY -> Calendar.YEAR
                PeriodType.MONTHLY -> Calendar.YEAR
                PeriodType.YEARLY -> Calendar.YEAR
            },
            direction
        )
    }

    fun adjustDate(
        date: Date,
        field: Int,
        amount: Int
    ): Date {
        val cal = Calendar.getInstance().apply { time = date }
        cal.add(field, amount)
        return cal.time
    }
}