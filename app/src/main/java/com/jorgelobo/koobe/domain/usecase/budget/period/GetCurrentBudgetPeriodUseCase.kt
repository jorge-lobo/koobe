package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.BudgetPeriod
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.utils.date.DateUtils
import javax.inject.Inject

class GetCurrentBudgetPeriodUseCase @Inject constructor(
    private val getBudgetPeriod: GetBudgetPeriodUseCase
) {

    operator fun invoke(
        periodType: PeriodType,
        startOfWeek: StartOfWeek
    ): BudgetPeriod = getBudgetPeriod(
        date = DateUtils.currentDate,
        periodType = periodType,
        startOfWeek = startOfWeek
    )
}