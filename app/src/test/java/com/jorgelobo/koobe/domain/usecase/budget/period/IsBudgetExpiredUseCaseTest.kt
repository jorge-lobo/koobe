package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsBudgetExpiredUseCaseTest {

    private val useCase = IsBudgetExpiredUseCase(
        getBudgetPeriod = GetBudgetPeriodUseCase()
    )

    @Test
    fun `budget is not expired before period end`() {
        val result = useCase(
            budget = fakeBudget(
                period = PeriodType.MONTHLY,
                periodStartDate = createDate(2026, Calendar.NOVEMBER, 1)
            ),
            date = createDate(2026, Calendar.NOVEMBER, 30),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertFalse(result)
    }

    @Test
    fun `budget is expired at period end`() {
        val result = useCase(
            budget = fakeBudget(
                period = PeriodType.MONTHLY,
                periodStartDate = createDate(2026, Calendar.NOVEMBER, 1)
            ),
            date = createDate(2026, Calendar.DECEMBER, 1),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertTrue(result)
    }

    @Test
    fun `budget is expired after period end`() {
        val result = useCase(
            budget = fakeBudget(
                period = PeriodType.MONTHLY,
                periodStartDate = createDate(2026, Calendar.NOVEMBER, 1)
            ),
            date = createDate(2026, Calendar.DECEMBER, 15),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertTrue(result)
    }

    @Test
    fun `budget expiration is calculated from its period start date`() {
        val result = useCase(
            budget = fakeBudget(
                period = PeriodType.MONTHLY,
                periodStartDate = createDate(2026, Calendar.NOVEMBER, 1)
            ),
            date = createDate(2027, Calendar.JANUARY, 15),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertTrue(result)
    }

    @Test
    fun `recurring budget does not expire automatically`() {
        val result = useCase(
            budget = fakeBudget(
                repeat = true,
                periodStartDate = createDate(2026, Calendar.NOVEMBER, 1)
            ),
            date = createDate(2027, Calendar.JANUARY, 15),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertFalse(result)
    }

    @Test
    fun `inactive non recurring budget can be expired`() {
        val result = useCase(
            budget = fakeBudget(
                isActive = false,
                periodStartDate = createDate(2026, Calendar.NOVEMBER, 1)
            ),
            date = createDate(2026, Calendar.DECEMBER, 1),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertTrue(result)
    }
}