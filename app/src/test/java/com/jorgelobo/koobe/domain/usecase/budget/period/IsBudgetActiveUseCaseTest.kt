package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsBudgetActiveUseCaseTest {

    private val useCase = IsBudgetActiveUseCase(
        isBudgetExpired = IsBudgetExpiredUseCase(
            getBudgetPeriod = GetBudgetPeriodUseCase()
        )
    )

    @Test
    fun `active non recurring budget within period`() {
        val result = useCase(
            budget = fakeBudget(),
            date = createDate(2026, Calendar.NOVEMBER, 15),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertTrue(result)
    }

    @Test
    fun `inactive budget is not active`() {
        val result = useCase(
            budget = fakeBudget(isActive = false),
            date = createDate(2026, Calendar.NOVEMBER, 15),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertFalse(result)
    }

    @Test
    fun `budget is not active before start date`() {
        val result = useCase(
            budget = fakeBudget(),
            date = createDate(2026, Calendar.OCTOBER, 31),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertFalse(result)
    }

    @Test
    fun `expired non recurring budget is not active`() {
        val result = useCase(
            budget = fakeBudget(),
            date = createDate(2026, Calendar.DECEMBER, 1),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertFalse(result)
    }

    @Test
    fun `active recurring budget remains active in next occurrence`() {
        val result = useCase(
            budget = fakeBudget(repeat = true),
            date = createDate(2027, Calendar.JANUARY, 15),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertTrue(result)
    }

    @Test
    fun `inactive recurring budget is not active`() {
        val result = useCase(
            budget = fakeBudget(
                repeat = true,
                isActive = false
            ),
            date = createDate(2026, Calendar.NOVEMBER, 15),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertFalse(result)
    }
}