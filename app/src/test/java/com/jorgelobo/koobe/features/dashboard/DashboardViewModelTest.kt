package com.jorgelobo.koobe.features.dashboard

import com.jorgelobo.koobe.domain.model.balance.PeriodTotals
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.model.settings.DefaultUserSettings
import com.jorgelobo.koobe.domain.repository.BudgetRepository
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.domain.settings.GetUserSettingsUseCase
import com.jorgelobo.koobe.domain.usecase.transaction.GetTransactionPeriodTotalsUseCase
import com.jorgelobo.koobe.ui.screen.dashboard.DashboardViewModel
import com.jorgelobo.koobe.utils.date.DateUtils
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val budgetRepository = mockk<BudgetRepository>()
    private val shortcutRepository = mockk<ShortcutRepository>()
    private val categoryRepository = mockk<CategoryRepository>()
    private val subcategoryRepository = mockk<SubcategoryRepository>()
    private val getTransactionPeriodTotals = mockk<GetTransactionPeriodTotalsUseCase>()
    private val getUserSettingsUseCase = mockk<GetUserSettingsUseCase>()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `balances should update ui state correctly`() = runTest {
        every { budgetRepository.getAllBudgets() } returns flowOf(emptyList())
        every { shortcutRepository.getAllShortcuts() } returns flowOf(emptyList())
        every { categoryRepository.getAllCategories() } returns flowOf(emptyList())
        every { subcategoryRepository.getAllSubcategories() } returns flowOf(emptyList())
        every { getUserSettingsUseCase() } returns flowOf(DefaultUserSettings)

        every { getTransactionPeriodTotals() } returns flowOf(
            PeriodTotals(
                income = 2000.0,
                expenses = 750.0
            )
        )

        val date = DateUtils.currentDate

        val dailyRange = DateUtils.getPeriodRange(
            date = date,
            periodType = PeriodType.DAILY
        )

        val weeklyRange = DateUtils.getPeriodRange(
            date = date,
            periodType = PeriodType.WEEKLY,
            startOfWeek = DefaultUserSettings.startOfWeek
        )

        val monthlyRange = DateUtils.getPeriodRange(
            date = date,
            periodType = PeriodType.MONTHLY,
            startOfWeek = DefaultUserSettings.startOfWeek
        )

        every {
            getTransactionPeriodTotals(
                dailyRange.first,
                dailyRange.second
            )
        } returns flowOf(
            PeriodTotals(
                income = 100.0,
                expenses = 25.0
            )
        )

        every {
            getTransactionPeriodTotals(
                weeklyRange.first,
                weeklyRange.second
            )
        } returns flowOf(
            PeriodTotals(
                income = 500.0,
                expenses = 200.0
            )
        )

        every {
            getTransactionPeriodTotals(
                monthlyRange.first,
                monthlyRange.second
            )
        } returns flowOf(
            PeriodTotals(
                income = 1200.0,
                expenses = 600.0
            )
        )

        val viewModel = createViewModel()

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals(1250.0, state.overallBalance)
        assertEquals(100.0, state.dailyIncome)
        assertEquals(25.0, state.dailyExpenses)
        assertEquals(500.0, state.weeklyIncome)
        assertEquals(200.0, state.weeklyExpenses)
        assertEquals(1200.0, state.income)
        assertEquals(600.0, state.expenses)
    }

    @Test
    fun `user settings should update currency and start of week`() = runTest {
        val settings = MutableStateFlow(DefaultUserSettings)

        every { budgetRepository.getAllBudgets() } returns flowOf(emptyList())
        every { shortcutRepository.getAllShortcuts() } returns flowOf(emptyList())
        every { categoryRepository.getAllCategories() } returns flowOf(emptyList())
        every { subcategoryRepository.getAllSubcategories() } returns flowOf(emptyList())
        every { getUserSettingsUseCase() } returns settings
        every { getTransactionPeriodTotals() } returns flowOf(PeriodTotals())
        every { getTransactionPeriodTotals(any(), any()) } returns flowOf(PeriodTotals())

        val viewModel = createViewModel()

        advanceUntilIdle()
        settings.value = DefaultUserSettings.copy(
            currency = CurrencyType.USD,
            startOfWeek = StartOfWeek.MONDAY
        )

        advanceUntilIdle()
        assertEquals(CurrencyType.USD, viewModel.uiState.value.currencyType)
        assertEquals(StartOfWeek.MONDAY, viewModel.uiState.value.startOfWeek)
    }

    private fun createViewModel() = DashboardViewModel(
        budgetRepository = budgetRepository,
        shortcutRepository = shortcutRepository,
        categoryRepository = categoryRepository,
        subcategoryRepository = subcategoryRepository,
        getTransactionPeriodTotals = getTransactionPeriodTotals,
        getUserSettingsUseCase = getUserSettingsUseCase
    )
}