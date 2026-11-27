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

class GetActiveBudgetsUseCaseTest {

    private val getAllBudgets = mockk<GetAllBudgetsUseCase>()
    private val isBudgetActive = mockk<IsBudgetActiveUseCase>()

    private val useCase = GetActiveBudgetsUseCase(
        getAllBudgets = getAllBudgets,
        isBudgetActive = isBudgetActive
    )

    @Test
    fun `return only active budgets`() = runTest {
        val activeBudget = fakeBudget(id = 1)
        val inactiveBudget = fakeBudget(id = 2)
        val budgets = listOf(activeBudget, inactiveBudget)

        every { getAllBudgets() } returns flowOf(budgets)

        every {
            isBudgetActive.invoke(any(), any(), any())
        } answers {
            firstArg<Budget>().id == activeBudget.id
        }

        val result = useCase(startOfWeek = StartOfWeek.MONDAY).first()

        assertEquals(listOf(activeBudget), result)
    }

    @Test
    fun `return empty list when no budgets are active`() = runTest {
        val budgets = listOf(
            fakeBudget(id = 1),
            fakeBudget(id = 2)
        )

        every { getAllBudgets() } returns flowOf(budgets)

        every {
            isBudgetActive.invoke(any(), any(), any())
        } returns false

        val result = useCase(startOfWeek = StartOfWeek.MONDAY).first()

        assertEquals(emptyList(), result)
    }

    @Test
    fun `emit updated active budgets when source flow changes`() = runTest {
        val firstBudget = fakeBudget(id = 1)
        val secondBudget = fakeBudget(id = 2)

        val source = kotlinx.coroutines.flow.MutableStateFlow(listOf(firstBudget))

        every { getAllBudgets() } returns source

        every {
            isBudgetActive.invoke(any(), any(), any())
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