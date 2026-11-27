package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.usecase.budget.GetAllBudgetsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class GetBudgetsForPeriodUseCaseTest {

    private val getAllBudgets = mockk<GetAllBudgetsUseCase>()
    private val isBudgetActive = mockk<IsBudgetActiveUseCase>()

    private val useCase = GetBudgetsForPeriodUseCase(
        getAllBudgets = getAllBudgets,
        isBudgetActive = isBudgetActive
    )

    @Test
    fun `return budgets active on requested date`() = runTest {
        val activeBudget = fakeBudget(id = 1)
        val inactiveBudget = fakeBudget(id = 2)

        every {
            getAllBudgets()
        } returns flowOf(listOf(activeBudget, inactiveBudget))

        every {
            isBudgetActive.invoke(any(), any(), any())
        } answers {
            firstArg<Budget>().id == activeBudget.id
        }

        val result = useCase(
            date = createDate(2026, java.util.Calendar.NOVEMBER, 15),
            startOfWeek = StartOfWeek.MONDAY
        ).first()

        assertEquals(listOf(activeBudget), result)
    }

    @Test
    fun `return empty list when no budgets are active on requested date`() = runTest {
        every {
            getAllBudgets()
        } returns flowOf(listOf(fakeBudget()))

        every {
            isBudgetActive.invoke(any(), any(), any())
        } returns false

        val result = useCase(
            date = createDate(2026, java.util.Calendar.DECEMBER, 15),
            startOfWeek = StartOfWeek.MONDAY
        ).first()

        assertEquals(emptyList(), result)
    }

    @Test
    fun `pass requested date to active budget validation`() = runTest {
        val budget = fakeBudget()
        val requestedDate = createDate(2026, java.util.Calendar.NOVEMBER, 20)

        every {
            getAllBudgets()
        } returns flowOf(listOf(budget))

        every {
            isBudgetActive.invoke(any(), any(), any())
        } returns true

        useCase(
            date = requestedDate,
            startOfWeek = StartOfWeek.MONDAY
        ).first()

        io.mockk.verify {
            isBudgetActive.invoke(
                budget = budget,
                date = requestedDate,
                startOfWeek = StartOfWeek.MONDAY
            )
        }
    }
}