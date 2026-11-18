package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals

class GetBudgetOccurrenceUseCaseTest {

    private val useCase = GetBudgetOccurrenceUseCase(
        getBudgetPeriod = GetBudgetPeriodUseCase()
    )

    @Test
    fun `get current occurrence`() {
        val budget = fakeBudget(repeat = true)

        val result = useCase(
            budget = budget,
            date = createDate(2026, Calendar.NOVEMBER, 13),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertEquals(
            createDate(2026, Calendar.NOVEMBER, 1),
            result.startDate
        )
        assertEquals(
            createDate(2026, Calendar.DECEMBER, 1),
            result.endDate
        )
    }

    @Test
    fun `get next occurrence`() {
        val budget = fakeBudget(repeat = true)

        val result = useCase(
            budget = budget,
            date = createDate(2026, Calendar.DECEMBER, 13),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertEquals(
            createDate(2026, Calendar.DECEMBER, 1),
            result.startDate
        )
        assertEquals(
            createDate(2027, Calendar.JANUARY, 1),
            result.endDate
        )
    }

    @Test
    fun `throw exception when budget is not recurring`() {
        val budget = fakeBudget(repeat = false)

        assertThrows(IllegalArgumentException::class.java) {
            useCase(
                budget = budget,
                date = createDate(2026, Calendar.NOVEMBER, 13),
                startOfWeek = StartOfWeek.MONDAY
            )
        }
    }

    @Test
    fun `throw exception when budget is inactive`() {
        val budget = fakeBudget(
            repeat = true,
            isActive = false
        )

        assertThrows(IllegalArgumentException::class.java) {
            useCase(
                budget = budget,
                date = createDate(2026, Calendar.NOVEMBER, 13),
                startOfWeek = StartOfWeek.MONDAY
            )
        }
    }

    @Test
    fun `throw exception when requested date is before budget start`() {
        val budget = fakeBudget(
            repeat = true,
            periodStartDate = createDate(2026, Calendar.NOVEMBER, 1)
        )

        assertThrows(IllegalArgumentException::class.java) {
            useCase(
                budget = budget,
                date = createDate(2026, Calendar.OCTOBER, 31),
                startOfWeek = StartOfWeek.MONDAY
            )
        }
    }

    @Test
    fun `throw exception when calculated period starts before budget start`() {
        val budget = fakeBudget(
            repeat = true,
            period = PeriodType.WEEKLY,
            periodStartDate = createDate(2026, Calendar.NOVEMBER, 10)
        )

        assertThrows(IllegalArgumentException::class.java) {
            useCase(
                budget = budget,
                date = createDate(2026, Calendar.NOVEMBER, 11),
                startOfWeek = StartOfWeek.MONDAY
            )
        }
    }
}