package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.BudgetPeriod
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.utils.date.DateUtils
import com.jorgelobo.koobe.utils.date.DateUtils.clearTime
import com.jorgelobo.koobe.utils.date.DateUtils.toCalendarValue
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class GetBudgetPeriodUseCase @Inject constructor() {

    operator fun invoke(
        date: Date,
        periodType: PeriodType,
        startOfWeek: StartOfWeek
    ): BudgetPeriod {
        val startDate = getPeriodStartDate(
            date = date,
            periodType = periodType,
            startOfWeek = startOfWeek
        )

        val endDate = getPeriodEndDate(
            startDate = startDate,
            periodType = periodType
        )

        return BudgetPeriod(
            startDate = startDate,
            endDate = endDate
        )
    }

    private fun getPeriodStartDate(
        periodType: PeriodType,
        date: Date,
        startOfWeek: StartOfWeek
    ): Date = DateUtils.withCalendar(date) {
        when (periodType) {
            PeriodType.DAILY -> {
                clearTime()
            }

            PeriodType.WEEKLY -> {
                firstDayOfWeek = startOfWeek.toCalendarValue()
                set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                clearTime()
            }

            PeriodType.MONTHLY -> {
                set(Calendar.DAY_OF_MONTH, 1)
                clearTime()
            }

            PeriodType.YEARLY -> {
                set(Calendar.DAY_OF_YEAR, 1)
                clearTime()
            }
        }

        time
    }

    private fun getPeriodEndDate(
        startDate: Date,
        periodType: PeriodType
    ): Date = DateUtils.withCalendar(startDate) {
        when (periodType) {
            PeriodType.DAILY -> add(Calendar.DAY_OF_MONTH, 1)
            PeriodType.WEEKLY -> add(Calendar.WEEK_OF_YEAR, 1)
            PeriodType.MONTHLY -> add(Calendar.MONTH, 1)
            PeriodType.YEARLY -> add(Calendar.YEAR, 1)
        }

        time
    }
}