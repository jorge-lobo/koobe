package com.jorgelobo.koobe.features.budgets.editor

import androidx.lifecycle.SavedStateHandle
import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.settings.DefaultUserSettings
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.settings.GetUserSettingsUseCase
import com.jorgelobo.koobe.domain.usecase.budget.DeleteBudgetUseCase
import com.jorgelobo.koobe.domain.usecase.budget.GetBudgetByIdUseCase
import com.jorgelobo.koobe.domain.usecase.budget.GetBudgetsBySubcategoryIdUseCase
import com.jorgelobo.koobe.domain.usecase.budget.HasExistingBudgetForPeriodUseCase
import com.jorgelobo.koobe.domain.usecase.budget.SaveBudgetUseCase
import com.jorgelobo.koobe.domain.usecase.category.GetCategoryByIdUseCase
import com.jorgelobo.koobe.domain.usecase.subcategory.GetSubcategoryByIdUseCase
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.budgets.editor.BudgetEditorConfig
import com.jorgelobo.koobe.ui.screen.budgets.editor.BudgetEditorEvent
import com.jorgelobo.koobe.ui.screen.budgets.editor.BudgetEditorIntent
import com.jorgelobo.koobe.ui.screen.budgets.editor.BudgetEditorViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BudgetEditorViewModelTest {

    private val getUserSettings: GetUserSettingsUseCase = mockk()
    private val getBudgetById: GetBudgetByIdUseCase = mockk()
    private val getCategoryById: GetCategoryByIdUseCase = mockk()
    private val getSubCategoryById: GetSubcategoryByIdUseCase = mockk()
    private val saveBudget: SaveBudgetUseCase = mockk()
    private val deleteBudget: DeleteBudgetUseCase = mockk()
    private val getBudgetsBySubcategoryId: GetBudgetsBySubcategoryIdUseCase = mockk()
    private val hasExistingBudgetForPeriod: HasExistingBudgetForPeriodUseCase = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        every { getUserSettings() } returns flowOf(DefaultUserSettings)

        coEvery { getCategoryById(1) } returns fakeCategory()
        coEvery { getSubCategoryById(10) } returns fakeSubcategory()
        coEvery { getBudgetById(100) } returns fakeBudget()

        every { getBudgetsBySubcategoryId(10) } returns flowOf(emptyList())

        coEvery {
            hasExistingBudgetForPeriod(any(), any(), any())
        } returns false

        coEvery {
            saveBudget(any(), any())
        } just runs

        coEvery {
            deleteBudget(any())
        } just runs
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // -------------------------------------------------------------------------
    // Initial state
    // -------------------------------------------------------------------------

    @Test
    fun `create mode should load category and subcategory`() = runTest {
        val viewModel = createViewModel(BudgetEditorConfig.Create(1, 10))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals(1, state.category.id)
        assertEquals(10, state.subcategory.id)
        assertEquals("0", state.limitAmountInput)
        assertEquals(PeriodType.MONTHLY, state.period)
        assertFalse(state.isRepeat)
        assertFalse(state.isSaving)
        assertFalse(state.isDeleting)

        coVerify { getCategoryById(1) }
        coVerify { getSubCategoryById(10) }
    }

    @Test
    fun `edit mode should load budget and related data`() = runTest {
        val viewModel = createViewModel(BudgetEditorConfig.Edit(100))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals(100, state.budget.id)
        assertEquals(1, state.category.id)
        assertEquals(10, state.subcategory.id)
        assertEquals("100.0", state.limitAmountInput)
        assertEquals(PeriodType.MONTHLY, state.period)
        assertFalse(state.isRepeat)
        assertFalse(state.isLoading)

        coVerify { getBudgetById(100) }
        coVerify { getCategoryById(1) }
        coVerify { getSubCategoryById(10) }
    }

    // -------------------------------------------------------------------------
    // Amount keypad
    // -------------------------------------------------------------------------

    @Test
    fun `amount keypad should append consecutive digits`() = runTest {
        val viewModel = createViewModel(BudgetEditorConfig.Create(1, 10))

        advanceUntilIdle()
        viewModel.onIntent(BudgetEditorIntent.State.AmountKeyPressed(KeypadKey.Digit(1)))
        viewModel.onIntent(BudgetEditorIntent.State.AmountKeyPressed(KeypadKey.Digit(2)))
        viewModel.onIntent(BudgetEditorIntent.State.AmountKeyPressed(KeypadKey.Digit(3)))

        advanceUntilIdle()
        assertEquals("123", viewModel.uiState.value.limitAmountInput)
        assertEquals(123.0, viewModel.uiState.value.limitAmount)
    }

    @Test
    fun `amount keypad should support decimal input`() = runTest {
        val viewModel = createViewModel(BudgetEditorConfig.Create(1, 10))

        advanceUntilIdle()
        viewModel.onIntent(BudgetEditorIntent.State.AmountKeyPressed(KeypadKey.Digit(1)))
        viewModel.onIntent(BudgetEditorIntent.State.AmountKeyPressed(KeypadKey.Decimal))
        viewModel.onIntent(BudgetEditorIntent.State.AmountKeyPressed(KeypadKey.Digit(5)))

        advanceUntilIdle()
        assertEquals("1.5", viewModel.uiState.value.limitAmountInput)
        assertEquals(1.5, viewModel.uiState.value.limitAmount)
    }

    @Test
    fun `amount reset should restore zero`() = runTest {
        val viewModel = createViewModel(BudgetEditorConfig.Create(1, 10))

        advanceUntilIdle()
        viewModel.onIntent(BudgetEditorIntent.State.AmountKeyPressed(KeypadKey.Digit(1)))
        viewModel.onIntent(BudgetEditorIntent.State.AmountKeyPressed(KeypadKey.Digit(2)))
        viewModel.onIntent(BudgetEditorIntent.State.AmountResetClicked)

        advanceUntilIdle()
        assertEquals("0", viewModel.uiState.value.limitAmountInput)
        assertEquals(0.0, viewModel.uiState.value.limitAmount)
    }

    @Test
    fun `amount keypad should reset existing amount on first edit`() = runTest {
        val viewModel = createViewModel(BudgetEditorConfig.Edit(100))

        advanceUntilIdle()
        viewModel.onIntent(BudgetEditorIntent.State.AmountKeyPressed(KeypadKey.Digit(5)))

        advanceUntilIdle()
        assertEquals("5", viewModel.uiState.value.limitAmountInput)
        assertEquals(5.0, viewModel.uiState.value.limitAmount)
    }

    // -------------------------------------------------------------------------
    // Form state
    // -------------------------------------------------------------------------

    @Test
    fun `repeat change should update form state`() = runTest {
        val viewModel = createViewModel(BudgetEditorConfig.Create(1, 10))

        advanceUntilIdle()
        viewModel.onIntent(BudgetEditorIntent.State.RepeatChanged(true))

        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isRepeat)
    }

    @Test
    fun `period change should update form state`() = runTest {
        val viewModel = createViewModel(BudgetEditorConfig.Create(1, 10))

        advanceUntilIdle()
        val newPeriod = PeriodType.YEARLY

        viewModel.onIntent(BudgetEditorIntent.State.PeriodChanged(newPeriod))

        advanceUntilIdle()
        assertEquals(newPeriod, viewModel.uiState.value.period)
    }

    // -------------------------------------------------------------------------
    // Save
    // -------------------------------------------------------------------------

    @Test
    fun `save should persist budget when form is valid and no duplicate exists`() = runTest {
        val viewModel = createViewModel(BudgetEditorConfig.Create(1, 10))

        advanceUntilIdle()
        viewModel.onIntent(BudgetEditorIntent.State.AmountKeyPressed(KeypadKey.Digit(1)))

        advanceUntilIdle()
        viewModel.onIntent(BudgetEditorIntent.Action.SaveClicked)

        advanceUntilIdle()
        coVerify {
            hasExistingBudgetForPeriod(
                subcategoryId = 10,
                period = PeriodType.MONTHLY,
                budgetId = null
            )
        }

        coVerify {
            saveBudget(
                match { it.categoryId == 1 && it.subcategoryId == 10 && it.limitAmount == 1.0 },
                false
            )
        }
    }

    @Test
    fun `save should show duplicate dialog when budget already exists`() = runTest {
        coEvery {
            hasExistingBudgetForPeriod(10, PeriodType.MONTHLY, null)
        } returns true

        val viewModel = createViewModel(BudgetEditorConfig.Create(1, 10))

        advanceUntilIdle()
        viewModel.onIntent(BudgetEditorIntent.State.AmountKeyPressed(KeypadKey.Digit(1)))

        advanceUntilIdle()
        viewModel.onIntent(BudgetEditorIntent.Action.SaveClicked)

        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.duplicateBudgetDialog.visible)

        coVerify(exactly = 0) {
            saveBudget(any(), any())
        }
    }

    @Test
    fun `save should exclude current budget when checking duplicates in edit mode`() = runTest {
        val viewModel = createViewModel(BudgetEditorConfig.Edit(100))

        advanceUntilIdle()
        viewModel.onIntent(BudgetEditorIntent.State.AmountKeyPressed(KeypadKey.Digit(5)))
        viewModel.onIntent(BudgetEditorIntent.Action.SaveClicked)

        advanceUntilIdle()
        coVerify {
            hasExistingBudgetForPeriod(
                subcategoryId = 10,
                period = PeriodType.MONTHLY,
                budgetId = 100
            )
        }
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    @Test
    fun `close should navigate back when there are no changes`() = runTest {
        val viewModel = createViewModel(BudgetEditorConfig.Create(1, 10))
        val events = collectEvents(viewModel)

        advanceUntilIdle()
        viewModel.onIntent(BudgetEditorIntent.Action.CloseClicked)

        advanceUntilIdle()
        assertTrue(events.contains(BudgetEditorEvent.ExitToOrigin))
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private fun TestScope.createViewModel(
        config: BudgetEditorConfig
    ): BudgetEditorViewModel {
        val savedStateHandle = SavedStateHandle(
            mapOf("config" to encodeConfig(config))
        )

        return BudgetEditorViewModel(
            savedStateHandle = savedStateHandle,
            getUserSettings = getUserSettings,
            getBudgetById = getBudgetById,
            getCategoryById = getCategoryById,
            getSubCategoryById = getSubCategoryById,
            saveBudget = saveBudget,
            deleteBudget = deleteBudget,
            getBudgetsBySubcategoryId = getBudgetsBySubcategoryId,
            hasExistingBudgetForPeriod = hasExistingBudgetForPeriod
        ).also { viewModel ->
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.uiState.collect()
            }
        }
    }

    private fun TestScope.collectEvents(
        viewModel: BudgetEditorViewModel
    ): MutableList<BudgetEditorEvent> {
        return mutableListOf<BudgetEditorEvent>().also { events ->
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.events.toList(events)
            }
        }
    }

    private fun encodeConfig(config: BudgetEditorConfig): String =
        URLEncoder.encode(
            Json.encodeToString(config),
            StandardCharsets.UTF_8.name()
        )

    private fun fakeCategory() = Category(
        id = 1,
        name = "Food",
        icon = IconPack.FOOD,
        color = "#FFFF00",
        type = TransactionType.EXPENSE
    )

    private fun fakeSubcategory() = Subcategory(
        id = 10,
        categoryId = 1,
        name = "Groceries",
        icon = IconPack.FOOD
    )

    private fun fakeBudget() = Budget(
        id = 100,
        categoryId = 1,
        subcategoryId = 10,
        period = PeriodType.MONTHLY,
        repeat = false,
        limitAmount = 100.0,
        spentAmount = 25.0,
        projectedAmount = 80.0,
        dailyAverage = 2.5
    )
}