package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals

class GetBudgetPeriodUseCaseTest {

    private val useCase = GetBudgetPeriodUseCase()

    @Test
    fun `get daily period`() {
        val result = useCase(
            date = createDate(2026, Calendar.NOVEMBER, 13),
            periodType = PeriodType.DAILY,
            startOfWeek = StartOfWeek.MONDAY
        )

        assertEquals(
            createDate(2026, Calendar.NOVEMBER, 13),
            result.startDate
        )
        assertEquals(
            createDate(2026, Calendar.NOVEMBER, 14),
            result.endDate
        )
    }

    @Test
    fun `get weekly period starting on monday`() {
        val result = useCase(
            date = createDate(2026, Calendar.NOVEMBER, 13),
            periodType = PeriodType.WEEKLY,
            startOfWeek = StartOfWeek.MONDAY
        )

        assertEquals(
            createDate(2026, Calendar.NOVEMBER, 9),
            result.startDate
        )
        assertEquals(
            createDate(2026, Calendar.NOVEMBER, 16),
            result.endDate
        )
    }

    @Test
    fun `get weekly period starting on sunday`() {
        val result = useCase(
            date = createDate(2026, Calendar.NOVEMBER, 13),
            periodType = PeriodType.WEEKLY,
            startOfWeek = StartOfWeek.SUNDAY
        )

        assertEquals(
            createDate(2026, Calendar.NOVEMBER, 8),
            result.startDate
        )
        assertEquals(
            createDate(2026, Calendar.NOVEMBER, 15),
            result.endDate
        )
    }

    @Test
    fun `get monthly period`() {
        val result = useCase(
            date = createDate(2026, Calendar.NOVEMBER, 13),
            periodType = PeriodType.MONTHLY,
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
    fun `get yearly period`() {
        val result = useCase(
            date = createDate(2026, Calendar.NOVEMBER, 13),
            periodType = PeriodType.YEARLY,
            startOfWeek = StartOfWeek.MONDAY
        )

        assertEquals(
            createDate(2026, Calendar.JANUARY, 1),
            result.startDate
        )
        assertEquals(
            createDate(2027, Calendar.JANUARY, 1),
            result.endDate
        )
    }

    @Test
    fun `get monthly period across year boundary`() {
        val result = useCase(
            date = createDate(2026, Calendar.DECEMBER, 20),
            periodType = PeriodType.MONTHLY,
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
}