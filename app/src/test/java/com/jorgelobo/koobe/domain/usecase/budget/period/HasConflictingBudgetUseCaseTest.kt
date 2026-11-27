package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.usecase.budget.GetAllBudgetsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HasConflictingBudgetUseCaseTest {

    private val getAllBudgets = mockk<GetAllBudgetsUseCase>()

    private val useCase = HasConflictingBudgetUseCase(
        getAllBudgets = getAllBudgets,
        getBudgetPeriod = GetBudgetPeriodUseCase()
    )

    @Test
    fun `return true when same subcategory and same period`() = runTest {
        val existing = fakeBudget(
            id = 1,
            period = PeriodType.MONTHLY,
            periodStartDate = createDate(2026, Calendar.NOVEMBER, 1)
        )

        val candidate = fakeBudget(
            id = 2,
            period = PeriodType.MONTHLY,
            periodStartDate = createDate(2026, Calendar.NOVEMBER, 1)
        )

        every { getAllBudgets() } returns flowOf(listOf(existing))

        val result = useCase(candidate, StartOfWeek.MONDAY)

        assertTrue(result)
    }

    @Test
    fun `return false when non recurring budgets belong to different periods`() = runTest {
        val existing = fakeBudget(
            id = 1,
            periodStartDate = createDate(2026, Calendar.NOVEMBER, 1)
        )

        val candidate = fakeBudget(
            id = 2,
            periodStartDate = createDate(2026, Calendar.DECEMBER, 1)
        )

        every { getAllBudgets() } returns flowOf(listOf(existing))

        val result = useCase(candidate, StartOfWeek.MONDAY)

        assertFalse(result)
    }

    @Test
    fun `return true when active recurring budget exists`() = runTest {
        val existing = fakeBudget(
            id = 1,
            repeat = true,
            periodStartDate = createDate(2026, Calendar.NOVEMBER, 1)
        )

        val candidate = fakeBudget(
            id = 2,
            periodStartDate = createDate(2026, Calendar.DECEMBER, 1)
        )

        every { getAllBudgets() } returns flowOf(listOf(existing))

        val result = useCase(candidate, StartOfWeek.MONDAY)

        assertTrue(result)
    }

    @Test
    fun `return false when existing recurring budget is inactive`() = runTest {
        val existing = fakeBudget(
            id = 1,
            repeat = true,
            isActive = false
        )

        val candidate = fakeBudget(id = 2)

        every { getAllBudgets() } returns flowOf(listOf(existing))

        val result = useCase(candidate, StartOfWeek.MONDAY)

        assertFalse(result)
    }

    @Test
    fun `return false when subcategories are different`() = runTest {
        val existing = fakeBudget(
            id = 1,
            subcategoryId = 10
        )

        val candidate = fakeBudget(
            id = 2,
            subcategoryId = 20
        )

        every { getAllBudgets() } returns flowOf(listOf(existing))

        val result = useCase(candidate, StartOfWeek.MONDAY)

        assertFalse(result)
    }

    @Test
    fun `return false when periods have different types`() = runTest {
        val existing = fakeBudget(
            id = 1,
            period = PeriodType.MONTHLY
        )

        val candidate = fakeBudget(
            id = 2,
            period = PeriodType.WEEKLY
        )

        every { getAllBudgets() } returns flowOf(listOf(existing))

        val result = useCase(candidate, StartOfWeek.MONDAY)

        assertFalse(result)
    }

    @Test
    fun `ignore candidate budget when editing`() = runTest {
        val existing = fakeBudget(id = 1)

        every { getAllBudgets() } returns flowOf(listOf(existing))

        val result = useCase(
            budget = existing.copy(limitAmount = 200.0),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertFalse(result)
    }

    @Test
    fun `ignore inactive existing non recurring budget`() = runTest {
        val existing = fakeBudget(
            id = 1,
            isActive = false
        )

        val candidate = fakeBudget(id = 2)

        every { getAllBudgets() } returns flowOf(listOf(existing))

        val result = useCase(candidate, StartOfWeek.MONDAY)

        assertFalse(result)
    }

    @Test
    fun `return false when no budgets exist`() = runTest {
        every { getAllBudgets() } returns flowOf(emptyList())

        val result = useCase(
            budget = fakeBudget(),
            startOfWeek = StartOfWeek.MONDAY
        )

        assertFalse(result)
    }
}