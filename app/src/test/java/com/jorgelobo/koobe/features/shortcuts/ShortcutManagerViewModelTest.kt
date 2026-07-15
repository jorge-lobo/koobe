package com.jorgelobo.koobe.features.shortcuts

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.SortingType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.usecase.category.GetAllCategoriesUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.DeleteShortcutUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.GetAllShortcutsByTypeUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.GetShortcutByIdUseCase
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.shortcuts.manager.ShortcutManagerEvent
import com.jorgelobo.koobe.ui.screen.shortcuts.manager.ShortcutManagerViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ShortcutManagerViewModelTest {

    private val getAllShortcuts: GetAllShortcutsByTypeUseCase = mockk()
    private val getAllCategories: GetAllCategoriesUseCase = mockk()
    private val getShortcutById: GetShortcutByIdUseCase = mockk()
    private val deleteShortcut: DeleteShortcutUseCase = mockk()


    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // region Init

    @Test
    fun `initial state should load expense shortcuts`() = runTest {
        every { getAllShortcuts(TransactionType.EXPENSE) } returns flowOf(emptyList())
        every { getAllCategories() } returns flowOf(emptyList())

        val viewModel = buildViewModel()
        val state = viewModel.uiState.first { !it.isLoading }

        assertEquals(TransactionType.EXPENSE, state.transactionTypeSelected)
        assertTrue(state.shortcutItems.isEmpty())
    }

    @Test
    fun `loading shortcuts failure should set error message`() = runTest {
        every { getAllShortcuts(any()) } returns flow { throw RuntimeException("Error") }
        every { getAllCategories() } returns flowOf(emptyList())

        val viewModel = buildViewModel()
        val state = viewModel.uiState.first { !it.isLoading }

        assertEquals("Error", state.errorMessage)
    }

    // endregion

    // region User Actions

    @Test
    fun `changing transaction type should update state`() = runTest {
        val viewModel = createViewModel()

        viewModel.onTransactionTypeChange(TransactionType.INCOME)
        advanceUntilIdle()

        assertEquals(
            TransactionType.INCOME,
            viewModel.uiState.value.transactionTypeSelected
        )
    }

    @Test
    fun `back click should navigate back`() = runTest {
        val viewModel = createViewModel()
        val events = mutableListOf<ShortcutManagerEvent>()
        val job = launch {
            viewModel.events.toList(events)
        }

        viewModel.onBackClick()
        advanceUntilIdle()

        assertTrue(events.contains(ShortcutManagerEvent.NavigateBack))
        job.cancel()
    }

    // endregion

    // region Delete

    @Test
    fun `delete click should open dialog`() = runTest {
        val viewModel = createViewModel()

        viewModel.onDeleteShortcutClick(7)
        advanceUntilIdle()

        val dialog = viewModel.uiState.value.deleteDialog

        assertTrue(dialog.visible)
        assertEquals(7, dialog.targetId)
    }

    @Test
    fun `delete dialog dismiss should close dialog`() = runTest {
        val viewModel = createViewModel()

        viewModel.onDeleteShortcutClick(3)
        advanceUntilIdle()

        viewModel.onDeleteDialogAction(ConfirmationDialogAction.Dismiss)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.deleteDialog.visible)
    }

    @Test
    fun `confirm delete should delete shortcut`() = runTest {
        val shortcut = fakeShortcut()

        coEvery { getShortcutById(2) } returns shortcut
        coEvery { deleteShortcut(shortcut) } just runs

        val viewModel = createViewModel()

        viewModel.onDeleteShortcutClick(2)
        advanceUntilIdle()

        viewModel.onDeleteDialogAction(ConfirmationDialogAction.Confirm)
        advanceUntilIdle()

        coVerify(exactly = 1) { deleteShortcut(shortcut) }
    }

    @Test
    fun `delete failure should set error message`() = runTest {
        val shortcut = fakeShortcut()

        coEvery { getShortcutById(2) } returns shortcut
        coEvery { deleteShortcut(shortcut) } throws RuntimeException("Delete failed")

        val viewModel = createViewModel()

        viewModel.onDeleteShortcutClick(2)
        advanceUntilIdle()

        viewModel.onDeleteDialogAction(ConfirmationDialogAction.Confirm)
        advanceUntilIdle()

        assertEquals("Delete failed", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `delete with invalid id should not call delete`() = runTest {
        coEvery { getShortcutById(any()) } returns null

        val viewModel = createViewModel()

        viewModel.onDeleteShortcutClick(5)
        advanceUntilIdle()

        viewModel.onDeleteDialogAction(ConfirmationDialogAction.Confirm)
        advanceUntilIdle()

        coVerify(exactly = 0) { deleteShortcut(any()) }
    }

    // endregion

    // region Sorting

    @Test
    fun `sorting click should open selector`() = runTest {
        val viewModel = createViewModel()

        viewModel.onSortingClick()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.sortingSelector.visible)
    }

    @Test
    fun `sorting cancel should close selector`() = runTest {
        val viewModel = createViewModel()

        viewModel.onSortingClick()
        advanceUntilIdle()

        viewModel.onSortingSheetAction(SelectorSheetAction.Dismiss)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.sortingSelector.visible)
    }

    @Test
    fun `sorting apply should update selected sorting`() = runTest {
        val viewModel = createViewModel()

        viewModel.onSortingClick()
        advanceUntilIdle()

        viewModel.onSortingSheetAction(SelectorSheetAction.Select(SortingType.NAME_ASC))
        advanceUntilIdle()

        assertEquals(
            SortingType.NAME_ASC,
            viewModel.uiState.value.sortingSelector.selected
        )
    }

    // endregion

    // region Helpers

    private fun createViewModel(): ShortcutManagerViewModel {
        every { getAllShortcuts(any()) } returns flowOf(emptyList())
        every { getAllCategories() } returns flowOf(emptyList())

        return buildViewModel()
    }

    private fun buildViewModel() = ShortcutManagerViewModel(
        getAllShortcuts = getAllShortcuts,
        getAllCategories = getAllCategories,
        getShortcutById = getShortcutById,
        deleteShortcut = deleteShortcut
    )

    private fun fakeShortcut(id: Int = 2, categoryId: Int = 1, name: String = "Bread") = Shortcut(
        id = id,
        name = name,
        icon = IconPack.FOOD,
        categoryId = categoryId,
        transactionType = TransactionType.EXPENSE,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR,
        amount = 3.50,
        repeat = false
    )

    // endregion
}