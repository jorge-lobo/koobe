package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.shortcut.Shortcut
import com.jorgelobo.koobe.utils.date.DateUtils
import java.util.Date
import javax.inject.Inject

/**
 * Determines whether a recurring shortcut is due for execution.
 *
 * Evaluates a shortcut based on its recurrence settings, execution period, and last execution date.
 */
class ShouldExecuteShortcutUseCase @Inject constructor() {

    /**
     * Returns `true` if the shortcut should be executed for the given [currentDate],
     * or `false` otherwise.
     */
    operator fun invoke(
        shortcut: Shortcut,
        currentDate: Date = DateUtils.currentDate
    ): Boolean {
        if (!shortcut.repeat) return false
        val period = shortcut.period ?: return false
        val lastExecution = shortcut.lastExecutionDate ?: return true

        return when (period) {
            PeriodType.DAILY -> !DateUtils.isSameDay(lastExecution, currentDate)
            PeriodType.WEEKLY -> !DateUtils.isSameWeek(lastExecution, currentDate)
            PeriodType.MONTHLY -> !DateUtils.isSameMonth(lastExecution, currentDate)
            PeriodType.YEARLY -> !DateUtils.isSameYear(lastExecution, currentDate)
        }
    }
}