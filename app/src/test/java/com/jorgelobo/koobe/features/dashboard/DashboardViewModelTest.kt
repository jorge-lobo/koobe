package com.jorgelobo.koobe.features.dashboard

import com.jorgelobo.koobe.domain.model.balance.PeriodTotals
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.settings.DefaultUserSettings
import com.jorgelobo.koobe.domain.model.shortcut.Shortcut
import com.jorgelobo.koobe.domain.settings.GetUserSettingsUseCase
import com.jorgelobo.koobe.domain.usecase.budget.GetAllBudgetsUseCase
import com.jorgelobo.koobe.domain.usecase.category.GetAllCategoriesUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.GetAllShortcutsUseCase
import com.jorgelobo.koobe.domain.usecase.subcategory.GetAllSubcategoriesUseCase
import com.jorgelobo.koobe.domain.usecase.transaction.GetTransactionPeriodTotalsUseCase
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.dashboard.DashboardViewModel
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

    private val getAllBudgets = mockk<GetAllBudgetsUseCase>()
    private val getAllShortcuts = mockk<GetAllShortcutsUseCase>()
    private val getAllCategories = mockk<GetAllCategoriesUseCase>()
    private val getAllSubcategories = mockk<GetAllSubcategoriesUseCase>()
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

    // region Balances

    @Test
    fun `balances should update ui state correctly`() = runTest {
        every { getAllBudgets() } returns flowOf(emptyList())
        every { getAllShortcuts() } returns flowOf(emptyList())
        every { getAllCategories() } returns flowOf(emptyList())
        every { getAllSubcategories() } returns flowOf(emptyList())
        every { getUserSettingsUseCase() } returns flowOf(DefaultUserSettings)

        every { getTransactionPeriodTotals() } returns flowOf(
            PeriodTotals(income = 2000.0, expenses = 750.0)
        )

        every { getTransactionPeriodTotals(any(), any()) } returnsMany listOf(
            // Monthly
            flowOf(PeriodTotals(income = 1200.0, expenses = 600.0)),

            // Daily
            flowOf(PeriodTotals(income = 100.0, expenses = 25.0)),

            // Weekly
            flowOf(PeriodTotals(income = 500.0, expenses = 200.0))
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
    fun `balances should react to start of week changes`() = runTest {
        val settings = MutableStateFlow(DefaultUserSettings)

        every { getAllBudgets() } returns flowOf(emptyList())
        every { getAllShortcuts() } returns flowOf(emptyList())
        every { getAllCategories() } returns flowOf(emptyList())
        every { getAllSubcategories() } returns flowOf(emptyList())
        every { getUserSettingsUseCase() } returns settings
        every { getTransactionPeriodTotals() } returns flowOf(PeriodTotals())
        every {
            getTransactionPeriodTotals(any(), any())
        } returns flowOf(PeriodTotals(income = 100.0, expenses = 25.0))

        val viewModel = createViewModel()

        advanceUntilIdle()
        assertEquals(100.0, viewModel.uiState.value.weeklyIncome)
        assertEquals(25.0, viewModel.uiState.value.weeklyExpenses)

        settings.value = DefaultUserSettings.copy(
            startOfWeek = StartOfWeek.MONDAY
        )

        advanceUntilIdle()
        assertEquals(StartOfWeek.MONDAY, viewModel.uiState.value.startOfWeek)
        assertEquals(100.0, viewModel.uiState.value.weeklyIncome)
        assertEquals(25.0, viewModel.uiState.value.weeklyExpenses)
    }

    // endregion

    // region User Settings

    @Test
    fun `user settings should update currency and start of week`() = runTest {
        val settings = MutableStateFlow(DefaultUserSettings)

        every { getAllBudgets() } returns flowOf(emptyList())
        every { getAllShortcuts() } returns flowOf(emptyList())
        every { getAllCategories() } returns flowOf(emptyList())
        every { getAllSubcategories() } returns flowOf(emptyList())
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

    // endregion

    // region Shortcuts

    @Test
    fun `shortcuts should be ordered by usage count descending`() = runTest {
        val shortcuts = listOf(
            fakeShortcut(id = 1, name = "Coffee", usageCount = 2),
            fakeShortcut(id = 2, name = "Groceries", usageCount = 8),
            fakeShortcut(id = 3, name = "Lunch", usageCount = 5)
        )

        every { getAllBudgets() } returns flowOf(emptyList())
        every { getAllShortcuts() } returns flowOf(shortcuts)
        every { getAllCategories() } returns flowOf(listOf(fakeCategory()))
        every { getAllSubcategories() } returns flowOf(emptyList())
        every { getUserSettingsUseCase() } returns flowOf(DefaultUserSettings)

        every { getTransactionPeriodTotals() } returns flowOf(PeriodTotals())
        every { getTransactionPeriodTotals(any(), any()) } returns flowOf(PeriodTotals())

        val viewModel = createViewModel()

        advanceUntilIdle()
        assertEquals(
            listOf(2, 3, 1),
            viewModel.uiState.value.shortcutItems.map { it.shortcut.id }
        )
    }

    @Test
    fun `shortcuts should be ordered by usage count and then alphabetically`() = runTest {
        val shortcuts = listOf(
            fakeShortcut(id = 1, name = "Lunch", usageCount = 5),
            fakeShortcut(id = 2, name = "Coffee", usageCount = 10),
            fakeShortcut(id = 3, name = "Groceries", usageCount = 5),
            fakeShortcut(id = 4, name = "Breakfast", usageCount = 10),
            fakeShortcut(id = 5, name = "Dinner", usageCount = 2)
        )

        every { getAllBudgets() } returns flowOf(emptyList())
        every { getAllShortcuts() } returns flowOf(shortcuts)
        every { getAllCategories() } returns flowOf(listOf(fakeCategory()))
        every { getAllSubcategories() } returns flowOf(emptyList())
        every { getUserSettingsUseCase() } returns flowOf(DefaultUserSettings)

        every { getTransactionPeriodTotals() } returns flowOf(PeriodTotals())
        every { getTransactionPeriodTotals(any(), any()) } returns flowOf(PeriodTotals())

        val viewModel = createViewModel()

        advanceUntilIdle()
        assertEquals(
            listOf(4, 2, 3),
            viewModel.uiState.value.shortcutItems.map { it.shortcut.id }
        )
    }

    // endregion

    private fun createViewModel() = DashboardViewModel(
        getTransactionPeriodTotals = getTransactionPeriodTotals,
        getAllBudgets = getAllBudgets,
        getAllShortcuts = getAllShortcuts,
        getAllCategories = getAllCategories,
        getAllSubcategories = getAllSubcategories,
        getUserSettingsUseCase = getUserSettingsUseCase
    )

    private fun fakeShortcut(
        id: Int,
        name: String,
        usageCount: Int
    ) = Shortcut(
        id = id,
        name = name,
        icon = IconPack.EXTRA,
        categoryId = 1,
        transactionType = TransactionType.EXPENSE,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR,
        amount = 0.0,
        usageCount = usageCount
    )

    private fun fakeCategory() = Category(
        id = 1,
        name = "Category",
        icon = IconPack.EXTRA,
        color = "#FFFFFF",
        type = TransactionType.EXPENSE
    )
}