package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.BudgetPeriod
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.utils.date.DateUtils
import javax.inject.Inject

/**
 * Use case responsible for calculating the active [BudgetPeriod] for the current date.
 *
 * This use case delegates to [GetBudgetPeriodUseCase], supplying [DateUtils.currentDate]
 * as the reference date along with the specified [PeriodType] and [StartOfWeek] settings.
 */
class GetCurrentBudgetPeriodUseCase @Inject constructor(
    private val getBudgetPeriod: GetBudgetPeriodUseCase
) {

    /**
     * Calculates the current [BudgetPeriod] based on the system's current date.
     *
     * @param periodType The frequency interval of the budget period ([PeriodType.DAILY], [PeriodType.WEEKLY], [PeriodType.MONTHLY], or [PeriodType.YEARLY]).
     * @param startOfWeek User preference specifying the first day of the week, used when calculating weekly period boundaries.
     * @return A [BudgetPeriod] covering the current time interval.
     */
    operator fun invoke(
        periodType: PeriodType,
        startOfWeek: StartOfWeek
    ): BudgetPeriod = getBudgetPeriod(
        date = DateUtils.currentDate,
        periodType = periodType,
        startOfWeek = startOfWeek
    )
}