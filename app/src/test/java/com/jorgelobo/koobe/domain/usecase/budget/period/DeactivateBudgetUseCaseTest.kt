package com.jorgelobo.koobe.domain.usecase.budget.period

import com.jorgelobo.koobe.domain.usecase.budget.UpdateBudgetUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeactivateBudgetUseCaseTest {

    private val updateBudget = mockk<UpdateBudgetUseCase>()

    private val useCase = DeactivateBudgetUseCase(
        updateBudget = updateBudget
    )

    @Test
    fun `deactivate active budget`() = runTest {
        val budget = fakeBudget(
            id = 1,
            isActive = true,
            repeat = true
        )

        coEvery {
            updateBudget(budget.copy(isActive = false))
        } just runs

        useCase(budget)

        coVerify(exactly = 1) {
            updateBudget(budget.copy(isActive = false))
        }
    }

    @Test
    fun `do not update already inactive budget`() = runTest {
        val budget = fakeBudget(
            id = 1,
            isActive = false
        )

        useCase(budget)

        coVerify(exactly = 0) {
            updateBudget(any())
        }
    }

    @Test
    fun `preserve recurring configuration when deactivating budget`() = runTest {
        val budget = fakeBudget(
            id = 1,
            isActive = true,
            repeat = true,
            limitAmount = 250.0
        )

        val expected = budget.copy(isActive = false)

        coEvery {
            updateBudget(expected)
        } just runs

        useCase(budget)

        coVerify(exactly = 1) {
            updateBudget(expected)
        }
    }
}