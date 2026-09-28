package com.jorgelobo.koobe.features.budgets.manager

import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.settings.DefaultUserSettings
import com.jorgelobo.koobe.domain.model.settings.UserSettings
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.settings.GetUserSettingsUseCase
import com.jorgelobo.koobe.domain.usecase.budget.GetAllBudgetsUseCase
import com.jorgelobo.koobe.domain.usecase.category.GetAllCategoriesUseCase
import com.jorgelobo.koobe.domain.usecase.subcategory.GetAllSubcategoriesUseCase
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.budgets.manager.BudgetManagerViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BudgetManagerViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val getAllBudgets = mockk<GetAllBudgetsUseCase>()
    private val getAllCategories = mockk<GetAllCategoriesUseCase>()
    private val getAllSubcategories = mockk<GetAllSubcategoriesUseCase>()
    private val getUserSettings = mockk<GetUserSettingsUseCase>()


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has default values`() = runTest {
        stubFlows()

        val viewModel = createViewModel()
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(emptyList(), state.periodicBudgets)
        assertEquals(CurrencyType.EUR, state.currencyType)
    }

    @Test
    fun `loads periodic budgets`() = runTest {
        val category = fakeCategory()
        val subcategory = fakeSubcategory()

        stubFlows(
            budgets = listOf(fakeBudget1()),
            categories = listOf(category),
            subcategories = listOf(subcategory)
        )

        val viewModel = createViewModel()

        runCurrent()
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(1, state.periodicBudgets.size)

        val periodicBudget = state.periodicBudgets.single()

        assertEquals(PeriodType.MONTHLY, periodicBudget.periodType)
        assertEquals(CurrencyType.EUR, periodicBudget.currencyType)
        assertEquals(1, periodicBudget.budgetsCount)
        assertEquals(500.0, periodicBudget.totalLimit)
        assertEquals(200.0, periodicBudget.totalSpent)
    }

    @Test
    fun `groups budgets by period`() = runTest {
        val category = fakeCategory()

        val budgets = listOf(
            fakeBudget1(),
            fakeBudget2(),
            fakeBudget3()
        )

        stubFlows(
            budgets = budgets,
            categories = listOf(category)
        )

        val viewModel = createViewModel()

        runCurrent()
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(2, state.periodicBudgets.size)

        val monthly = state.periodicBudgets
            .first { it.periodType == PeriodType.MONTHLY }

        assertEquals(2, monthly.budgetsCount)
        assertEquals(800.0, monthly.totalLimit)
        assertEquals(300.0, monthly.totalSpent)

        val weekly = state.periodicBudgets
            .first { it.periodType == PeriodType.WEEKLY }

        assertEquals(1, weekly.budgetsCount)
        assertEquals(100.0, weekly.totalLimit)
        assertEquals(40.0, weekly.totalSpent)
    }

    @Test
    fun `ignores budgets when category does not exist`() = runTest {
        val budgets = listOf(
            fakeBudget(
                id = 1,
                categoryId = 999,
                period = PeriodType.MONTHLY
            )
        )

        stubFlows(
            budgets = budgets,
            categories = emptyList()
        )

        val viewModel = createViewModel()

        runCurrent()
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertTrue(state.periodicBudgets.isEmpty())
    }

    @Test
    fun `uses nullable subcategory when budget has no subcategory`() = runTest {
        val category = fakeCategory()

        stubFlows(
            budgets = listOf(fakeBudget2()),
            categories = listOf(category),
            subcategories = emptyList()
        )

        val viewModel = createViewModel()

        runCurrent()
        val state = viewModel.uiState.value

        val budgetItem = state.periodicBudgets
            .single()
            .budgets
            .single()

        assertEquals(null, budgetItem.subcategory)
        assertEquals(category, budgetItem.category)
    }

    @Test
    fun `updates currency when user settings change`() = runTest {
        val settingsFlow = MutableStateFlow(
            DefaultUserSettings.copy(currency = CurrencyType.EUR)
        )

        stubFlows(
            userSettings = settingsFlow
        )

        val viewModel = createViewModel()

        advanceUntilIdle()
        assertEquals(CurrencyType.EUR, viewModel.uiState.value.currencyType)

        settingsFlow.value = DefaultUserSettings.copy(
            currency = CurrencyType.USD
        )

        advanceUntilIdle()
        assertEquals(CurrencyType.USD, viewModel.uiState.value.currencyType)
    }

    @Test
    fun `periodic budgets use current user currency`() = runTest {
        val settingsFlow = MutableStateFlow(
            DefaultUserSettings.copy(currency = CurrencyType.EUR)
        )

        stubFlows(
            budgets = listOf(fakeBudget1()),
            categories = listOf(fakeCategory()),
            userSettings = settingsFlow
        )

        val viewModel = createViewModel()

        advanceUntilIdle()
        assertEquals(
            CurrencyType.EUR,
            viewModel.uiState.value.periodicBudgets.single().currencyType
        )

        settingsFlow.value = DefaultUserSettings.copy(
            currency = CurrencyType.USD
        )

        advanceUntilIdle()
        assertEquals(
            CurrencyType.USD,
            viewModel.uiState.value.periodicBudgets.single().currencyType
        )
    }

    private fun createViewModel(): BudgetManagerViewModel {
        return BudgetManagerViewModel(
            getAllBudgets = getAllBudgets,
            getAllCategories = getAllCategories,
            getAllSubcategories = getAllSubcategories,
            getUserSettings = getUserSettings
        )
    }

    private fun stubFlows(
        budgets: List<Budget> = emptyList(),
        categories: List<Category> = emptyList(),
        subcategories: List<Subcategory> = emptyList(),
        userSettings: Flow<UserSettings> =
            flowOf(DefaultUserSettings)
    ) {
        every { getAllBudgets() } returns flowOf(budgets)
        every { getAllCategories() } returns flowOf(categories)
        every { getAllSubcategories() } returns flowOf(subcategories)
        every { getUserSettings() } returns userSettings
    }

    private fun fakeBudget(
        id: Int = 1,
        categoryId: Int = 1,
        subcategoryId: Int? = null,
        period: PeriodType = PeriodType.MONTHLY,
        limitAmount: Double = 500.0,
        spentAmount: Double = 100.0
    ) = Budget(
        id = id,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        period = period,
        repeat = false,
        paymentMethod = null,
        currency = CurrencyType.EUR,
        limitAmount = limitAmount,
        spentAmount = spentAmount,
        projectedAmount = spentAmount,
        dailyAverage = 10.0
    )

    private fun fakeCategory() = Category(
        id = 1,
        name = "Category name",
        icon = IconPack.PLACEHOLDER,
        color = "#FFFFFF",
        type = TransactionType.EXPENSE
    )

    private fun fakeSubcategory() = Subcategory(
        id = 10,
        categoryId = 1,
        name = "Subcategory name",
        icon = IconPack.PLACEHOLDER
    )

    private fun fakeBudget1() = Budget(
        id = 1,
        categoryId = 1,
        period = PeriodType.MONTHLY,
        limitAmount = 500.0,
        spentAmount = 200.0,
        subcategoryId = 10,
        repeat = false,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR,
        projectedAmount = 450.0,
        dailyAverage = 15.0
    )

    private fun fakeBudget2() = Budget(
        id = 2,
        categoryId = 1,
        period = PeriodType.MONTHLY,
        limitAmount = 300.0,
        spentAmount = 100.0,
        subcategoryId = null,
        repeat = false,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR,
        projectedAmount = 275.0,
        dailyAverage = 15.0
    )

    private fun fakeBudget3() = Budget(
        id = 3,
        categoryId = 1,
        period = PeriodType.WEEKLY,
        limitAmount = 100.0,
        spentAmount = 40.0,
        subcategoryId = null,
        repeat = false,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR,
        projectedAmount = 95.0,
        dailyAverage = 12.0
    )
}