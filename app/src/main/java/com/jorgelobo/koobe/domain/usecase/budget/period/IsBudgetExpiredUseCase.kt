package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.utils.date.DateUtils
import java.util.Date
import javax.inject.Inject

class IsBudgetExpiredUseCase @Inject constructor(
    private val getBudgetPeriod: GetBudgetPeriodUseCase
) {

    operator fun invoke(
        budget: Budget,
        date: Date = DateUtils.currentDate,
        startOfWeek: StartOfWeek
    ): Boolean {
        if (budget.repeat) return false

        val period = getBudgetPeriod(
            date = budget.periodStartDate,
            periodType = budget.period,
            startOfWeek = startOfWeek
        )

        return !date.before(period.endDate)
    }
}