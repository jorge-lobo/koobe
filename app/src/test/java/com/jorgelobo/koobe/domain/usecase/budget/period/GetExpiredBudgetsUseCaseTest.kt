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

class GetExpiredBudgetsUseCaseTest {

    private val getAllBudgets = mockk<GetAllBudgetsUseCase>()
    private val isBudgetExpired = mockk<IsBudgetExpiredUseCase>()

    private val useCase = GetExpiredBudgetsUseCase(
        getAllBudgets = getAllBudgets,
        isBudgetExpired = isBudgetExpired
    )

    @Test
    fun `return only expired budgets`() = runTest {
        val expiredBudget = fakeBudget(id = 1)
        val activeBudget = fakeBudget(id = 2)

        every {
            getAllBudgets()
        } returns flowOf(listOf(expiredBudget, activeBudget))

        every {
            isBudgetExpired.invoke(any(), any(), any())
        } answers {
            firstArg<Budget>().id == expiredBudget.id
        }

        val result = useCase(startOfWeek = StartOfWeek.MONDAY).first()

        assertEquals(listOf(expiredBudget), result)
    }

    @Test
    fun `return empty list when no budgets are expired`() = runTest {
        every {
            getAllBudgets()
        } returns flowOf(listOf(fakeBudget()))

        every {
            isBudgetExpired.invoke(any(), any(), any())
        } returns false

        val result = useCase(startOfWeek = StartOfWeek.MONDAY).first()

        assertEquals(emptyList(), result)
    }

    @Test
    fun `emit updated expired budgets when source flow changes`() = runTest {
        val firstBudget = fakeBudget(id = 1)
        val secondBudget = fakeBudget(id = 2)

        val source = kotlinx.coroutines.flow.MutableStateFlow(listOf(firstBudget))

        every { getAllBudgets() } returns source

        every {
            isBudgetExpired.invoke(any(), any(), any())
        } returns true

        val resultFlow = useCase(StartOfWeek.MONDAY)

        assertEquals(listOf(firstBudget), resultFlow.first())

        source.value = listOf(firstBudget, secondBudget)

        assertEquals(
            listOf(firstBudget, secondBudget),
            resultFlow.first()
        )
    }
}