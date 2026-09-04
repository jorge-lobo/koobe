package com.jorgelobo.koobe.domain.usecase.transaction

import com.jorgelobo.koobe.domain.model.balance.PeriodTotals
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.domain.repository.TransactionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetTransactionPeriodTotalsUseCaseTest {

    private val repository = mockk<TransactionRepository>()

    private lateinit var useCase: GetTransactionPeriodTotalsUseCase

    @Before
    fun setup() {
        useCase = GetTransactionPeriodTotalsUseCase(repository)
    }

    @Test
    fun `invoke should return zero totals when transaction list is empty`() = runTest {
        every { repository.getAllTransactions() } returns flowOf(emptyList())

        val result = useCase().first()

        assertEquals(0.0, result.income)
        assertEquals(0.0, result.expenses)
        assertEquals(0.0, result.balance)
    }

    @Test
    fun `invoke should calculate income totals correctly`() = runTest {
        val transactions = listOf(
            transaction(
                type = TransactionType.INCOME,
                amount = 100.0
            ),
            transaction(
                type = TransactionType.INCOME,
                amount = 250.0
            )
        )

        every { repository.getAllTransactions() } returns flowOf(transactions)

        val result = useCase().first()

        assertEquals(350.0, result.income)
        assertEquals(0.0, result.expenses)
        assertEquals(350.0, result.balance)
    }

    @Test
    fun `invoke should calculate expense totals correctly`() = runTest {
        val transactions = listOf(
            transaction(
                type = TransactionType.EXPENSE,
                amount = 50.0
            ),
            transaction(
                type = TransactionType.EXPENSE,
                amount = 75.0
            )
        )

        every { repository.getAllTransactions() } returns flowOf(transactions)

        val result = useCase().first()

        assertEquals(0.0, result.income)
        assertEquals(125.0, result.expenses)
        assertEquals(-125.0, result.balance)
    }

    @Test
    fun `invoke should calculate totals correctly for mixed transactions`() = runTest {
        val transactions = listOf(
            transaction(
                type = TransactionType.INCOME,
                amount = 1000.0
            ),
            transaction(
                type = TransactionType.INCOME,
                amount = 500.0
            ),
            transaction(
                type = TransactionType.EXPENSE,
                amount = 300.0
            ),
            transaction(
                type = TransactionType.EXPENSE,
                amount = 200.0
            )
        )

        every { repository.getAllTransactions() } returns flowOf(transactions)

        val result = useCase().first()

        assertEquals(1500.0, result.income)
        assertEquals(500.0, result.expenses)
        assertEquals(1000.0, result.balance)
    }

    @Test
    fun `invoke with period should request transactions using provided date range`() = runTest {
        val startDate = Date(1_000L)
        val endDate = Date(2_000L)

        every {
            repository.getTransactionsByPeriod(
                startDate = startDate.time,
                endDate = endDate.time
            )
        } returns flowOf(emptyList())

        val result = useCase(
            startDate = startDate,
            endDate = endDate
        ).first()

        assertEquals(PeriodTotals(), result)

        verify(exactly = 1) {
            repository.getTransactionsByPeriod(
                startDate = startDate.time,
                endDate = endDate.time
            )
        }
    }

    @Test
    fun `invoke with period should calculate totals correctly`() = runTest {
        val startDate = Date(1_000L)
        val endDate = Date(2_000L)

        val transactions = listOf(
            transaction(
                type = TransactionType.INCOME,
                amount = 500.0
            ),
            transaction(
                type = TransactionType.EXPENSE,
                amount = 125.0
            )
        )

        every {
            repository.getTransactionsByPeriod(
                startDate = startDate.time,
                endDate = endDate.time
            )
        } returns flowOf(transactions)

        val result = useCase(
            startDate = startDate,
            endDate = endDate
        ).first()

        assertEquals(500.0, result.income)
        assertEquals(125.0, result.expenses)
        assertEquals(375.0, result.balance)
    }

    private fun transaction(
        type: TransactionType,
        amount: Double
    ) = Transaction(
        id = null,
        date = Date(),
        description = "",
        type = type,
        categoryId = 1,
        subcategoryId = null,
        amount = amount,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR
    )
}