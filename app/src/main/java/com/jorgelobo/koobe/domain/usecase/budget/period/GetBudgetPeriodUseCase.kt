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

/**
 * Use case responsible for calculating the start and end dates of a [BudgetPeriod].
 *
 * Given a reference date, a [PeriodType] (daily, weekly, monthly, or yearly), and a user-configured
 * [StartOfWeek], this use case computes the normalized start date (with time components cleared to 00:00:00)
 * and the corresponding end date for the budget period.
 */
class GetBudgetPeriodUseCase @Inject constructor() {

    /**
     * Calculates the [BudgetPeriod] enclosing the given [date].
     *
     * @param date The reference date used to locate the period boundaries.
     * @param periodType The frequency interval of the budget period ([PeriodType.DAILY], [PeriodType.WEEKLY], [PeriodType.MONTHLY], or [PeriodType.YEARLY]).
     * @param startOfWeek User preference specifying the first day of the week, used when calculating weekly period boundaries.
     * @return A [BudgetPeriod] containing the calculated normalized start date and end date.
     */
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

    /**
     * Computes the normalized start date for the given [periodType] enclosing [date].
     *
     * Clears the time components (hours, minutes, seconds, milliseconds) to midnight and aligns the date
     * to the start of the specified period:
     * - [PeriodType.DAILY]: Same day at 00:00:00.
     * - [PeriodType.WEEKLY]: First day of the week based on [startOfWeek] at 00:00:00.
     * - [PeriodType.MONTHLY]: First day of the month at 00:00:00.
     * - [PeriodType.YEARLY]: First day of the year at 00:00:00.
     *
     * @param periodType The type of budget period.
     * @param date The reference date.
     * @param startOfWeek User preference defining the starting day of the week.
     * @return The normalized start [Date].
     */
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

    /**
     * Computes the end date for the budget period given its [startDate] and [periodType].
     *
     * Adds the corresponding time interval (1 day, 1 week, 1 month, or 1 year) to [startDate].
     *
     * @param startDate The start date of the period.
     * @param periodType The type of budget period.
     * @return The calculated end [Date].
     */
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