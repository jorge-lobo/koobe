package com.jorgelobo.koobe.domain.usecase.shortcut

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.utils.date.DateUtils
import java.util.Date
import javax.inject.Inject

class ShouldExecuteShortcutUseCase @Inject constructor() {
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