package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.utils.date.DateUtils
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GetCurrentBudgetPeriodUseCaseTest {

    private val useCase = GetCurrentBudgetPeriodUseCase(
        getBudgetPeriod = GetBudgetPeriodUseCase()
    )

    @Test
    fun `get current monthly period`() {
        val referenceDate = DateUtils.currentDate

        val result = useCase(
            periodType = PeriodType.MONTHLY,
            startOfWeek = StartOfWeek.MONDAY
        )

        assertFalse(referenceDate.before(result.startDate))
        assertTrue(referenceDate.before(result.endDate))
    }

    @Test
    fun `get current weekly period`() {
        val referenceDate = DateUtils.currentDate

        val result = useCase(
            periodType = PeriodType.WEEKLY,
            startOfWeek = StartOfWeek.MONDAY
        )

        assertFalse(referenceDate.before(result.startDate))
        assertTrue(referenceDate.before(result.endDate))
    }
}