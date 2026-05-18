package com.jorgelobo.koobe.features.subcategories

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.domain.usecase.subcategory.DeleteSubcategoryWithReassignUseCase
import com.jorgelobo.koobe.domain.usecase.subcategory.SaveSubcategoryCaseUse
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorEvent
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorIntent
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.URLEncoder
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SubcategoryEditorViewModelTest {

    private val subcategoryRepository: SubcategoryRepository = mockk()
    private val categoryRepository: CategoryRepository = mockk()
    private val saveSubcategory: SaveSubcategoryCaseUse = mockk(relaxed = true)
    private val deleteSubcategory: DeleteSubcategoryWithReassignUseCase = mockk(relaxed = true)

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
    fun `create mode should emit empty subcategory and category from flow`() = runTest {
        val viewModel = createViewModelCreateMode()

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals("", state.subcategory.name)
        assertEquals(1, state.category.id)
    }

    @Test
    fun `edit mode should load subcategory from flow`() = runTest {
        val viewModel = createViewModelEditMode()

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals("Groceries", state.subcategory.name)
        assertEquals(1, state.category.id)
    }

    @Test
    fun `uiState should update when flow emits new value`() = runTest {
        val flow = MutableStateFlow<Subcategory?>(null)
        val category = fakeCategory()

        val viewModel = createViewModel(
            SubcategoryEditorConfig(2, 1),
            subcategoryFlow = flow,
            categoryFlow = flowOf(category)
        )

        viewModel.uiState.test {
            skipItems(1)

            val initial = awaitItem()
            assertEquals("", initial.subcategory.name)

            flow.value = fakeSubcategory("Updated Name")

            val updated = awaitItem()
            assertEquals("Updated Name", updated.subcategory.name)
        }
    }

    @Test
    fun `edit mode should emit error state when subcategory not found`() = runTest {
        val viewModel = createViewModel(
            config = SubcategoryEditorConfig(2, 1),
            subcategoryFlow = flowOf(null),
            categoryFlow = flowOf(fakeCategory())
        )

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals("Subcategory not found", state.errorMessage)
    }

    // endregion

    // region Form State

    @Test
    fun `onNameChanged should update subcategory name`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(SubcategoryEditorIntent.State.NameChanged("New Name"))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals("New Name", state.subcategory.name)
    }

    @Test
    fun `onResetName should reset subcategory name`() = runTest {
        val viewModel = createViewModelEditMode()

        advanceUntilIdle()
        viewModel.onIntent(SubcategoryEditorIntent.State.NameChanged(""))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals("", state.subcategory.name)
    }

    @Test
    fun `onIconSelected should update icon`() = runTest {
        val viewModel = createViewModelCreateMode()
        val icon = IconPack.FOOD

        viewModel.onIntent(
            SubcategoryEditorIntent.Action.IconSelectorDialogAction(
                SelectorDialogAction.Select(icon)
            )
        )
        viewModel.onIntent(
            SubcategoryEditorIntent.Action.IconSelectorDialogAction(
                SelectorDialogAction.Apply
            )
        )

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals(icon, state.subcategory.icon)
    }

    @Test
    fun `onCategoryChanged should update categoryId`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(SubcategoryEditorIntent.State.CategoryChanged(5))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals(5, state.subcategory.categoryId)
    }

    // endregion

    // region Validation

    @Test
    fun `create mode should enable save when valid`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(SubcategoryEditorIntent.State.NameChanged("Food"))
        viewModel.onIntent(SubcategoryEditorIntent.State.IconSelected(IconPack.FOOD))
        viewModel.onIntent(SubcategoryEditorIntent.State.CategoryChanged(1))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertTrue(state.isSaveEnabled)
    }

    @Test
    fun `create mode should disable save when name is blank`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(SubcategoryEditorIntent.State.NameChanged(""))
        viewModel.onIntent(SubcategoryEditorIntent.State.IconSelected(IconPack.FOOD))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertFalse(state.isSaveEnabled)
    }

    @Test
    fun `edit mode should enable save when changed`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onIntent(SubcategoryEditorIntent.State.NameChanged("New Name"))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertTrue(state.isSaveEnabled)
    }

    @Test
    fun `edit mode should disable save when invalid`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onIntent(SubcategoryEditorIntent.State.NameChanged(""))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertFalse(state.isSaveEnabled)
    }

    // endregion

    // region Events - Save

    @Test
    fun `successful save should emit navigate back event`() = runTest {
        coEvery { saveSubcategory(any(), any()) } just runs

        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(SubcategoryEditorIntent.State.NameChanged("Food"))
        viewModel.onIntent(SubcategoryEditorIntent.State.IconSelected(IconPack.FOOD))
        viewModel.onIntent(SubcategoryEditorIntent.State.CategoryChanged(1))

        advanceUntilIdle()
        val events = mutableListOf<SubcategoryEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(SubcategoryEditorIntent.Action.SaveClicked)

        advanceUntilIdle()
        assertTrue(events.contains(SubcategoryEditorEvent.NavigateBack))
        job.cancel()
    }

    @Test
    fun `save failure should emit snackbar event`() = runTest {
        coEvery { saveSubcategory(any(), any()) } throws RuntimeException()

        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(SubcategoryEditorIntent.State.NameChanged("Food"))
        viewModel.onIntent(SubcategoryEditorIntent.State.IconSelected(IconPack.FOOD))
        viewModel.onIntent(SubcategoryEditorIntent.State.CategoryChanged(1))

        advanceUntilIdle()
        val events = mutableListOf<SubcategoryEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(SubcategoryEditorIntent.Action.SaveClicked)

        advanceUntilIdle()
        assertTrue(events.any { it is SubcategoryEditorEvent.ShowSnackBar })
        job.cancel()
    }

    @Test
    fun `save should set loading state`() = runTest {
        coEvery { saveSubcategory(any(), any()) } coAnswers {
            delay(100)
        }

        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(SubcategoryEditorIntent.State.NameChanged("Food"))
        viewModel.onIntent(SubcategoryEditorIntent.State.IconSelected(IconPack.FOOD))
        viewModel.onIntent(SubcategoryEditorIntent.State.CategoryChanged(1))

        advanceTimeBy(1)
        viewModel.onIntent(SubcategoryEditorIntent.Action.SaveClicked)

        advanceTimeBy(1)
        val state = viewModel.uiState.value

        assertTrue(state.isSaving)
    }

    @Test
    fun `edit mode should disable save when unchanged`() = runTest {
        val viewModel = createViewModelEditMode()

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertFalse(state.isSaveEnabled)
    }

    @Test
    fun `create mode should disable save when icon is placeholder`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(SubcategoryEditorIntent.State.NameChanged("Food"))
        viewModel.onIntent(SubcategoryEditorIntent.State.CategoryChanged(1))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertFalse(state.isSaveEnabled)
    }

    // endregion

    // region Delete Flow

    @Test
    fun `delete should navigate back on success`() = runTest {
        coEvery { deleteSubcategory(any()) } just runs

        val viewModel = createViewModelEditMode()
        val events = mutableListOf<SubcategoryEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(SubcategoryEditorIntent.Action.RequestDeleteSubcategory)

        advanceUntilIdle()
        viewModel.onIntent(
            SubcategoryEditorIntent.Action.DeleteDialogAction(
                ConfirmationDialogAction.Confirm
            )
        )

        advanceUntilIdle()
        assertTrue(events.contains(SubcategoryEditorEvent.NavigateBack))
        job.cancel()
    }

    @Test
    fun `delete should emit snackbar on failure`() = runTest {
        coEvery { deleteSubcategory(any()) } throws RuntimeException()

        val viewModel = createViewModelEditMode()
        val events = mutableListOf<SubcategoryEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(SubcategoryEditorIntent.Action.RequestDeleteSubcategory)

        advanceUntilIdle()
        viewModel.onIntent(
            SubcategoryEditorIntent.Action.DeleteDialogAction(
                ConfirmationDialogAction.Confirm
            )
        )

        advanceUntilIdle()
        assertTrue(events.any { it is SubcategoryEditorEvent.ShowSnackBar })
        job.cancel()
    }

    @Test
    fun `delete should set loading state`() = runTest {
        coEvery { deleteSubcategory(any()) } coAnswers {
            delay(100)
        }

        val viewModel = createViewModelEditMode()

        viewModel.onIntent(SubcategoryEditorIntent.Action.RequestDeleteSubcategory)

        advanceUntilIdle()
        viewModel.onIntent(
            SubcategoryEditorIntent.Action.DeleteDialogAction(
                ConfirmationDialogAction.Confirm
            )
        )

        runCurrent()
        val state = viewModel.uiState.value

        assertTrue(state.isDeleting)
    }

    // endregion

    // region Dialogs

    @Test
    fun `onCloseClick should open discard dialog when unsaved changes`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onIntent(SubcategoryEditorIntent.State.NameChanged("Changed"))

        advanceUntilIdle()
        viewModel.onIntent(SubcategoryEditorIntent.Action.CloseClicked)

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertTrue(state.discardDialog.visible)
    }

    @Test
    fun `confirm discard should navigate back`() = runTest {
        val viewModel = createViewModelEditMode()
        val events = mutableListOf<SubcategoryEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(SubcategoryEditorIntent.State.NameChanged("Changed"))

        advanceUntilIdle()
        viewModel.onIntent(SubcategoryEditorIntent.Action.CloseClicked)

        advanceUntilIdle()
        viewModel.onIntent(
            SubcategoryEditorIntent.Action.DiscardDialogAction(
                ConfirmationDialogAction.Confirm
            )
        )

        advanceUntilIdle()
        assertTrue(events.contains(SubcategoryEditorEvent.NavigateBack))
        job.cancel()
    }

    @Test
    fun `icon selector apply should update icon`() = runTest {
        val viewModel = createViewModelCreateMode()

        val icon = IconPack.FOOD

        viewModel.onIntent(
            SubcategoryEditorIntent.Action.IconSelectorDialogAction(
                SelectorDialogAction.Select(icon)
            )
        )
        viewModel.onIntent(
            SubcategoryEditorIntent.Action.IconSelectorDialogAction(
                SelectorDialogAction.Apply
            )
        )

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals(icon, state.subcategory.icon)
    }

    @Test
    fun `discard dialog cancel should close dialog`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onIntent(SubcategoryEditorIntent.State.NameChanged("Changed"))

        advanceUntilIdle()
        viewModel.onIntent(SubcategoryEditorIntent.Action.CloseClicked)

        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.discardDialog.visible)

        viewModel.onIntent(
            SubcategoryEditorIntent.Action.DiscardDialogAction(
                ConfirmationDialogAction.Dismiss
            )
        )

        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.discardDialog.visible)
    }

    @Test
    fun `delete dialog dismiss should close dialog`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onIntent(SubcategoryEditorIntent.Action.RequestDeleteSubcategory)

        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.deleteDialog.visible)

        viewModel.onIntent(
            SubcategoryEditorIntent.Action.DeleteDialogAction(
                ConfirmationDialogAction.Dismiss
            )
        )

        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.deleteDialog.visible)
    }

    @Test
    fun `icon selector dismiss should not change icon`() = runTest {
        val viewModel = createViewModelCreateMode()

        val initialIcon = viewModel.uiState.value.subcategory.icon

        viewModel.onIntent(
            SubcategoryEditorIntent.Action.IconSelectorDialogAction(
                SelectorDialogAction.Select(IconPack.FOOD)
            )
        )
        viewModel.onIntent(
            SubcategoryEditorIntent.Action.IconSelectorDialogAction(
                SelectorDialogAction.Cancel
            )
        )

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals(initialIcon, state.subcategory.icon)
    }

    // endregion

    // region Helpers

    private fun createViewModel(
        config: SubcategoryEditorConfig,
        subcategoryFlow: Flow<Subcategory?> = flowOf(null),
        categoryFlow: Flow<Category?> = flowOf(Category.empty())
    ): SubcategoryEditorViewModel {

        every { subcategoryRepository.getSubcategoryByIdFlow(any()) } returns subcategoryFlow
        every { categoryRepository.getCategoryByIdFlow(any()) } returns categoryFlow

        val savedStateHandle = SavedStateHandle(
            mapOf("config" to encodeConfig(config))
        )

        return SubcategoryEditorViewModel(
            savedStateHandle,
            subcategoryRepository,
            categoryRepository,
            saveSubcategory,
            deleteSubcategory
        )
    }

    private fun createViewModelCreateMode() =
        createViewModel(
            SubcategoryEditorConfig(null, 1),
            categoryFlow = flowOf(fakeCategory())
        )

    private fun createViewModelEditMode() =
        createViewModel(
            SubcategoryEditorConfig(2, 1),
            subcategoryFlow = flowOf(fakeSubcategory()),
            categoryFlow = flowOf(fakeCategory())
        )

    private fun fakeCategory(id: Int = 1) = Category(
        id = id,
        name = "Food",
        icon = IconPack.FOOD,
        color = "FF00FF",
        type = TransactionType.EXPENSE
    )

    private fun fakeSubcategory(name: String = "Groceries") = Subcategory(
        id = 2,
        name = name,
        icon = IconPack.FOOD,
        categoryId = 1
    )

    private fun encodeConfig(config: SubcategoryEditorConfig): String {
        return URLEncoder.encode(Json.encodeToString(config), "UTF-8")
    }

    // endregion
}