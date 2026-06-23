package com.jorgelobo.koobe.features.shortcuts

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import com.jorgelobo.koobe.domain.usecase.shortcut.DeleteShortcutUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.SaveShortcutUseCase
import com.jorgelobo.koobe.domain.validation.NameValidationException
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorConfig
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorEvent
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorIntent
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import java.net.URLEncoder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ShortcutEditorViewModelTest {

    private val shortcutRepository: ShortcutRepository = mockk()
    private val categoryRepository: CategoryRepository = mockk()
    private val saveShortcut: SaveShortcutUseCase = mockk(relaxed = true)
    private val deleteShortcut: DeleteShortcutUseCase = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // region Initial State

    @Test
    fun `create mode should emit empty shortcut from flow`() = runTest {
        val viewModel = createViewModelCreateMode()

        val state = viewModel.uiState.first { it.config != null }

        assertEquals("", state.name)
        assertEquals(1, state.category.id)
    }

    @Test
    fun `create mode should load category from config`() = runTest {
        val category = fakeCategory(id = 10)
        val viewModel = createViewModel(
            config = ShortcutEditorConfig.Create(10),
            categoryFlow = flowOf(category)
        )

        val state = viewModel.uiState.first { it.config != null }

        assertEquals(category, state.category)
        assertTrue(state.config is ShortcutEditorConfig.Create)
    }

    @Test
    fun `edit mode should load shortcut from flow`() = runTest {
        val viewModel = createViewModelEditMode()

        val state = viewModel.uiState.first { it.config != null }

        assertEquals("Bread", state.name)
        assertEquals(1, state.category.id)
    }

    @Test
    fun `should return empty state when category is null`() = runTest {
        val viewModel = createViewModel(
            config = ShortcutEditorConfig.Create(1),
            categoryFlow = flowOf(null)
        )

        val state = viewModel.uiState.first()

        assertTrue(state.config == null)
        assertTrue(state.name.isEmpty())
    }

    @Test
    fun `uiState should update when form state changes`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.uiState.test {
            val initial = awaitItem()
            assertEquals("", initial.name)

            viewModel.onIntent(ShortcutEditorIntent.State.NameChanged("New Name"))

            val updated = awaitItem()
            assertEquals("New Name", updated.name)
        }
    }

    @Test
    fun `uiState should update when flow emits new value`() = runTest {
        val flow = MutableStateFlow<Shortcut?>(fakeShortcut())

        val viewModel = createViewModel(
            config = ShortcutEditorConfig.Edit(1),
            shortcutFlow = flow,
            categoryFlow = flowOf(fakeCategory())
        )

        viewModel.uiState.test {
            skipItems(1)

            val initial = awaitItem()
            assertEquals("Bread", initial.name)

            flow.value = fakeShortcut(name = "Updated")

            val updated = awaitItem()
            assertEquals("Updated", updated.name)
        }
    }

    // endregion

    // region Form State

    @Test
    fun `onNameChanged should update shortcut name`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(ShortcutEditorIntent.State.NameChanged("New Name"))

        val state = viewModel.uiState.first { it.config != null }

        assertEquals("New Name", state.name)
    }

    @Test
    fun `onIconSelected should update icon`() = runTest {
        val viewModel = createViewModelCreateMode()
        val icon = IconPack.FOOD

        viewModel.onIntent(
            ShortcutEditorIntent.Action.IconSelectorDialogUpdated(
                SelectorDialogAction.Select(icon)
            )
        )
        viewModel.onIntent(
            ShortcutEditorIntent.Action.IconSelectorDialogUpdated(
                SelectorDialogAction.Apply
            )
        )

        val state = viewModel.uiState.first { it.config != null }

        assertEquals(icon, state.icon)
    }

    // endregion

    // region Validation

    @Test
    fun `create mode should enable save when valid`() = runTest {
        val viewModel = createViewModelCreateMode()
        viewModel.fillValidData()

        val state = viewModel.uiState.first { it.config != null }

        assertTrue(state.isSaveEnabled)
    }

    @Test
    fun `create mode should disable save when name is blank`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(ShortcutEditorIntent.State.NameChanged(""))
        viewModel.onIntent(ShortcutEditorIntent.State.IconChanged(IconPack.FOOD))

        val state = viewModel.uiState.first { it.config != null }

        assertFalse(state.isSaveEnabled)
    }

    @Test
    fun `edit mode should enable save when changed`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onIntent(ShortcutEditorIntent.State.NameChanged("New Name"))

        val state = viewModel.uiState.first { it.config != null }

        assertTrue(state.isSaveEnabled)
    }

    @Test
    fun `edit mode should disable save when invalid`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onIntent(ShortcutEditorIntent.State.NameChanged(""))

        val state = viewModel.uiState.first { it.config != null }

        assertFalse(state.isSaveEnabled)
    }

    @Test
    fun `duplicate name should emit snackbar`() = runTest {
        coEvery {
            saveShortcut(any(), any())
        } throws NameValidationException.Duplicate()

        val viewModel = createViewModelCreateMode()
        viewModel.fillValidData()

        viewModel.onIntent(ShortcutEditorIntent.Action.SaveClicked)

        val event = viewModel.events.first() as ShortcutEditorEvent.ShowSnackbar

        assertEquals(R.string.snackBar_save_shortcut_error, event.messageRes)
    }

    // endregion

    // region Save

    @Test
    fun `save emits snackbar when form is invalid`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(ShortcutEditorIntent.Action.SaveClicked)

        assertEquals(
            ShortcutEditorEvent.ShowSnackbar(
                R.string.snackBar_save_shortcut_error,
                null,
                IconPack.WARNING
            ),
            viewModel.events.first()
        )

        coVerify(exactly = 0) { saveShortcut(any(), any()) }
    }

    @Test
    fun `successful save should emit navigate back event`() = runTest {
        coEvery { saveShortcut(any(), any()) } just runs

        val viewModel = createViewModelCreateMode()
        viewModel.uiState.first { it.config != null }
        viewModel.fillValidData()

        advanceUntilIdle()
        val events = mutableListOf<ShortcutEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(ShortcutEditorIntent.Action.SaveClicked)

        advanceUntilIdle()
        assertTrue(events.any { it is ShortcutEditorEvent.NavigateBack })
        job.cancel()
    }

    @Test
    fun `save failure should emit snackbar event`() = runTest {
        coEvery { saveShortcut(any(), any()) } throws RuntimeException()

        val viewModel = createViewModelCreateMode()
        viewModel.fillValidData()

        advanceUntilIdle()
        val events = mutableListOf<ShortcutEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(ShortcutEditorIntent.Action.SaveClicked)

        advanceUntilIdle()
        assertTrue(events.any { it is ShortcutEditorEvent.ShowSnackbar })
        job.cancel()
    }

    @Test
    fun `save should set isSaving state`() = runTest {
        coEvery { saveShortcut(any(), any()) } coAnswers {
            delay(100)
        }

        val viewModel = createViewModelCreateMode()
        viewModel.uiState.first { it.config != null }
        viewModel.fillValidData()

        advanceTimeBy(1)
        viewModel.onIntent(ShortcutEditorIntent.Action.SaveClicked)

        advanceTimeBy(1)
        assertTrue(viewModel.uiState.value.isSaving)
    }

    @Test
    fun `save error should emit snackbar after state update`() = runTest {
        coEvery { saveShortcut(any(), any()) } throws RuntimeException()

        val viewModel = createViewModelCreateMode()
        viewModel.fillValidData()

        viewModel.onIntent(ShortcutEditorIntent.Action.SaveClicked)

        val events = viewModel.events.take(1).toList()

        assertTrue(events.first() is ShortcutEditorEvent.ShowSnackbar)
    }

    @Test
    fun `edit mode should disable save when unchanged`() = runTest {
        val viewModel = createViewModelEditMode()

        val state = viewModel.uiState.first { it.config != null }

        assertFalse(state.isSaveEnabled)
    }

    @Test
    fun `create mode should disable save when icon is placeholder`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(ShortcutEditorIntent.State.NameChanged("Food"))
        viewModel.onIntent(ShortcutEditorIntent.State.CategoryChanged(1))

        val state = viewModel.uiState.first { it.config != null }

        assertFalse(state.isSaveEnabled)
    }

    // endregion

    // region Delete Flow

    @Test
    fun `delete should call use case`() = runTest {
        coEvery { deleteShortcut(any()) } just runs

        val viewModel = createViewModelEditMode()
        viewModel.uiState.first { it.config != null }
        val events = mutableListOf<ShortcutEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(ShortcutEditorIntent.Action.RequestDeleteShortcut)
        advanceUntilIdle()
        viewModel.onIntent(
            ShortcutEditorIntent.Action.DeleteDialogUpdated(
                ConfirmationDialogAction.Confirm
            )
        )

        advanceUntilIdle()
        coVerify { deleteShortcut(any()) }
        job.cancel()
    }

    @Test
    fun `delete should navigate back on success`() = runTest {
        coEvery { deleteShortcut(any()) } just runs

        val viewModel = createViewModelEditMode()
        viewModel.uiState.first { it.config != null }
        val events = mutableListOf<ShortcutEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(ShortcutEditorIntent.Action.RequestDeleteShortcut)

        advanceUntilIdle()
        viewModel.onIntent(
            ShortcutEditorIntent.Action.DeleteDialogUpdated(
                ConfirmationDialogAction.Confirm
            )
        )

        advanceUntilIdle()
        assertTrue(events.contains(ShortcutEditorEvent.NavigateBack))
        job.cancel()
    }

    @Test
    fun `delete should emit snackbar on failure`() = runTest {
        coEvery { deleteShortcut(any()) } throws RuntimeException()

        val viewModel = createViewModelEditMode()
        viewModel.uiState.first { it.config != null }
        val events = mutableListOf<ShortcutEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(ShortcutEditorIntent.Action.RequestDeleteShortcut)

        advanceUntilIdle()
        viewModel.onIntent(
            ShortcutEditorIntent.Action.DeleteDialogUpdated(
                ConfirmationDialogAction.Confirm
            )
        )

        advanceUntilIdle()
        assertTrue(events.any { it is ShortcutEditorEvent.ShowSnackbar })
        job.cancel()
    }

    // endregion

    // region Errors

    @Test
    fun `save with generic error should emit generic snackbar`() = runTest {
        coEvery { saveShortcut(any(), any()) } throws RuntimeException()

        val viewModel = createViewModelCreateMode()
        viewModel.fillValidData()

        viewModel.onIntent(ShortcutEditorIntent.Action.SaveClicked)

        val event = viewModel.events.first() as ShortcutEditorEvent.ShowSnackbar

        assertEquals(R.string.snackBar_save_shortcut_error, event.messageRes)
    }

    // endregion

    // region Close

    @Test
    fun `close should navigate back when form has no changes`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(ShortcutEditorIntent.Action.CloseClicked)

        assertEquals(ShortcutEditorEvent.NavigateBack, viewModel.events.first())
    }

    // endregion

    // region Dialogs

    @Test
    fun `onCloseClick should open discard dialog when unsaved changes`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onIntent(ShortcutEditorIntent.State.NameChanged("Changed"))

        advanceUntilIdle()
        viewModel.onIntent(ShortcutEditorIntent.Action.CloseClicked)

        val state = viewModel.uiState.first { it.config != null }

        assertTrue(state.discardDialog.visible)
    }

    @Test
    fun `confirm discard should navigate back`() = runTest {
        val viewModel = createViewModelEditMode()
        val events = mutableListOf<ShortcutEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(ShortcutEditorIntent.State.NameChanged("Changed"))

        advanceUntilIdle()
        viewModel.onIntent(ShortcutEditorIntent.Action.CloseClicked)

        advanceUntilIdle()
        viewModel.onIntent(
            ShortcutEditorIntent.Action.DiscardDialogUpdated(
                ConfirmationDialogAction.Confirm
            )
        )

        advanceUntilIdle()
        assertTrue(events.contains(ShortcutEditorEvent.NavigateBack))
        job.cancel()
    }

    @Test
    fun `icon selector apply should update icon`() = runTest {
        val viewModel = createViewModelCreateMode()
        val icon = IconPack.FOOD

        viewModel.onIntent(
            ShortcutEditorIntent.Action.IconSelectorDialogUpdated(
                SelectorDialogAction.Select(icon)
            )
        )
        viewModel.onIntent(
            ShortcutEditorIntent.Action.IconSelectorDialogUpdated(
                SelectorDialogAction.Apply
            )
        )

        val state = viewModel.uiState.first { it.config != null }

        assertEquals(icon, state.icon)
    }

    @Test
    fun `discard dialog cancel should close dialog`() = runTest {
        val viewModel = createViewModelEditMode()
        val collector = launch {
            viewModel.uiState.collect()
        }

        viewModel.uiState.first { it.config != null }

        viewModel.onIntent(ShortcutEditorIntent.State.NameChanged("Changed"))

        advanceUntilIdle()
        viewModel.onIntent(ShortcutEditorIntent.Action.CloseClicked)

        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.discardDialog.visible)

        viewModel.onIntent(
            ShortcutEditorIntent.Action.DiscardDialogUpdated(
                ConfirmationDialogAction.Dismiss
            )
        )

        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.discardDialog.visible)
        collector.cancel()
    }

    @Test
    fun `delete dialog dismiss should close dialog`() = runTest {
        val viewModel = createViewModelEditMode()
        val collector = launch {
            viewModel.uiState.collect()
        }

        viewModel.uiState.first { it.config != null }

        viewModel.onIntent(ShortcutEditorIntent.Action.RequestDeleteShortcut)

        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.deleteDialog.visible)

        viewModel.onIntent(
            ShortcutEditorIntent.Action.DeleteDialogUpdated(
                ConfirmationDialogAction.Dismiss
            )
        )

        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.deleteDialog.visible)
        collector.cancel()
    }

    @Test
    fun `icon selector cancel should revert to previous icon`() = runTest {
        val viewModel = createViewModelCreateMode()
        val collector = launch {
            viewModel.uiState.collect()
        }

        viewModel.uiState.first { it.config != null }

        val initialIcon = viewModel.uiState.value.icon

        viewModel.onIntent(
            ShortcutEditorIntent.Action.IconSelectorDialogUpdated(
                SelectorDialogAction.Select(IconPack.FOOD)
            )
        )
        viewModel.onIntent(
            ShortcutEditorIntent.Action.IconSelectorDialogUpdated(
                SelectorDialogAction.Cancel
            )
        )

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals(initialIcon, state.icon)
        collector.cancel()
    }

    // endregion

    // region Helpers

    private fun createViewModel(
        config: ShortcutEditorConfig,
        shortcutFlow: Flow<Shortcut?> = flowOf(null),
        categoryFlow: Flow<Category?> = flowOf(Category.empty())
    ): ShortcutEditorViewModel {

        every { shortcutRepository.getShortcutByIdFlow(any()) } returns shortcutFlow
        every { categoryRepository.getCategoryByIdFlow(any()) } returns categoryFlow

        val savedStateHandle = SavedStateHandle(
            mapOf("config" to encodeConfig(config))
        )

        return ShortcutEditorViewModel(
            savedStateHandle = savedStateHandle,
            shortcutRepository = shortcutRepository,
            categoryRepository = categoryRepository,
            saveShortcut = saveShortcut,
            deleteShortcut = deleteShortcut
        )
    }

    private fun createViewModelCreateMode() =
        createViewModel(
            config = ShortcutEditorConfig.Create(1),
            shortcutFlow = flowOf(null),
            categoryFlow = flowOf(fakeCategory())
        )

    private fun createViewModelEditMode() =
        createViewModel(
            config = ShortcutEditorConfig.Edit(2),
            shortcutFlow = flowOf(fakeShortcut()),
            categoryFlow = flowOf(fakeCategory())
        )

    private fun ShortcutEditorViewModel.fillValidData() {
        onIntent(ShortcutEditorIntent.State.NameChanged("New Name"))
        onIntent(ShortcutEditorIntent.State.IconChanged(IconPack.ENTERTAINMENT))
        onIntent(ShortcutEditorIntent.State.AmountResetClicked)
        onIntent(ShortcutEditorIntent.State.AmountKeyPressed(KeypadKey.Digit(1)))
    }

    private fun fakeCategory(id: Int = 1) = Category(
        id = id,
        name = "Food",
        icon = IconPack.FOOD,
        color = "FF00FF",
        type = TransactionType.EXPENSE
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

    private fun encodeConfig(config: ShortcutEditorConfig): String {
        return URLEncoder.encode(Json.encodeToString(config), "UTF-8")
    }

    // endregion
}