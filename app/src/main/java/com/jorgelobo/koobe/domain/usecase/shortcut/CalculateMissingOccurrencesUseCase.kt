package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.utils.date.DateUtils
import com.jorgelobo.koobe.utils.date.DateUtils.clearTime
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
 * Calculates the occurrences of a recurring shortcut that have not yet been executed.
 *
 * Returns all missing execution dates between the shortcut's last execution and the provided
 * current date.
 */
class CalculateMissingOccurrencesUseCase @Inject constructor() {

    /**
     * Calculates all scheduled execution dates that are missing for the given [shortcut].
     *
     * Occurrences are calculated from the shortcut's last execution date up to the current date,
     * according to its configured recurrence period. If the shortcut has no previous execution
     * date, the current day is returned as its first occurrence.
     *
     * @param shortcut The recurring shortcut for which missing occurrences are calculated.
     * @param currentDate The date used as the upper bound when calculating occurrences.
     * @return A list of missing execution dates in chronological order.
     */
    operator fun invoke(
        shortcut: Shortcut,
        currentDate: Date = DateUtils.currentDate
    ): List<Date> {
        if (!shortcut.repeat) return emptyList()

        val period = shortcut.period ?: return emptyList()
        val lastExecution = shortcut.lastExecutionDate ?: return listOf(currentDate.clearTime())
        val today = currentDate.clearTime()
        val calendar = Calendar.getInstance().apply {
            time = lastExecution.clearTime()
        }

        val occurrences = mutableListOf<Date>()

        while (true) {
            when (period) {
                PeriodType.DAILY -> calendar.add(Calendar.DAY_OF_YEAR, 1)
                PeriodType.WEEKLY -> calendar.add(Calendar.WEEK_OF_YEAR, 1)
                PeriodType.MONTHLY -> calendar.add(Calendar.MONTH, 1)
                PeriodType.YEARLY -> calendar.add(Calendar.YEAR, 1)
            }

            if (calendar.time.after(today)) break

            occurrences += calendar.time
        }
        return occurrences
    }
}