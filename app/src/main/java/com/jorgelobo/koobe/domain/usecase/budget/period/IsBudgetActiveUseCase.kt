package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.utils.date.DateUtils
import java.util.Date
import javax.inject.Inject

class IsBudgetActiveUseCase @Inject constructor(
    private val isBudgetExpired: IsBudgetExpiredUseCase
) {

    operator fun invoke(
        budget: Budget,
        date: Date = DateUtils.currentDate,
        startOfWeek: StartOfWeek
    ): Boolean {
        if (!budget.isActive) return false

        if (date.before(budget.periodStartDate)) return false

        return !isBudgetExpired(
            budget = budget,
            date = date,
            startOfWeek = startOfWeek
        )
    }
}