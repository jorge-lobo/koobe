package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import java.util.Calendar
import java.util.Date

internal fun createDate(
    year: Int,
    month: Int,
    day: Int
): Date = Calendar.getInstance().apply {
    clear()
    set(year, month, day, 0, 0, 0)
}.time

internal fun fakeBudget(
    id: Int = 1,
    subcategoryId: Int = 10,
    period: PeriodType = PeriodType.MONTHLY,
    periodStartDate: Date = createDate(2026, Calendar.NOVEMBER, 1),
    isActive: Boolean = true,
    repeat: Boolean = false,
    limitAmount: Double = 100.0
) = Budget(
    id = id,
    categoryId = 1,
    subcategoryId = subcategoryId,
    period = period,
    periodStartDate = periodStartDate,
    isActive = isActive,
    repeat = repeat,
    limitAmount = limitAmount,
    spentAmount = 0.0,
    projectedAmount = 0.0,
    dailyAverage = 0.0
)