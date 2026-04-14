package com.jorgelobo.koobe.features.subcategories

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.domain.usecase.subcategory.DeleteSubcategoryWithReassignUseCase
import com.jorgelobo.koobe.domain.usecase.subcategory.SaveSubcategoryCaseUse
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorEvent
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorUiState
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
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

        val category = fakeCategory()

        val viewModel = createViewModel(
            SubcategoryEditorConfig(null, 1),
            subcategoryFlow = flowOf(null),
            categoryFlow = flowOf(category)
        )

        val state = viewModel.uiState.awaitSecondItem()

        assertEquals("", state.subcategory.name)
        assertEquals(1, state.category.id)

    }

    @Test
    fun `edit mode should load subcategory from flow`() = runTest {

        val subcategory = fakeSubcategory()
        val category = fakeCategory()

        val viewModel = createViewModel(
            SubcategoryEditorConfig(2, 1),
            subcategoryFlow = flowOf(subcategory),
            categoryFlow = flowOf(category)
        )

        val state = viewModel.uiState.awaitSecondItem()

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

    // endregion

    // region Form State

    @Test
    fun `onNameChanged should update subcategory name`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onNameChanged("New Name")

        val state = viewModel.awaitState { it.subcategory.name == "New Name" }

        assertEquals("New Name", state.subcategory.name)
    }

    @Test
    fun `onResetName should reset subcategory name`() = runTest {

        val flow = MutableStateFlow(fakeSubcategory(name = "Initial Name"))

        val viewModel = createViewModel(
            SubcategoryEditorConfig(2, 1),
            subcategoryFlow = flow,
            categoryFlow = flowOf(fakeCategory())
        )

        viewModel.awaitState { it.subcategory.name == "Initial Name" }

        viewModel.onResetName()

        val updated = viewModel.awaitState { it.subcategory.name == "" }

        assertEquals("", updated.subcategory.name)
    }

    @Test
    fun `onIconSelected should update icon`() = runTest {
        val viewModel = createViewModelCreateMode()

        val icon = IconPack.FOOD
        viewModel.onIconSelected(icon)

        val state = viewModel.awaitState { it.subcategory.icon == icon }

        assertEquals(icon, state.subcategory.icon)
    }

    @Test
    fun `onCategoryChanged should update categoryId`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onCategoryChanged(5)

        val state = viewModel.awaitState { it.subcategory.categoryId == 5 }

        assertEquals(5, state.subcategory.categoryId)
    }

    // endregion

    // region Validation

    @Test
    fun `create mode should enable save when valid`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onNameChanged("Food")
        viewModel.onIconSelected(IconPack.FOOD)
        viewModel.onCategoryChanged(1)

        advanceUntilIdle()

        val state = viewModel.awaitState { it.isSaveButtonEnabled }

        assertTrue(state.isSaveButtonEnabled)
    }

    @Test
    fun `create mode should disable save when name is blank`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onNameChanged("")
        viewModel.onIconSelected(IconPack.FOOD)

        val state = viewModel.awaitState { !it.isSaveButtonEnabled }

        assertFalse(state.isSaveButtonEnabled)
    }

    @Test
    fun `edit mode should enable save when changed`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onNameChanged("New Name")

        val state = viewModel.awaitState { it.isSaveButtonEnabled }

        assertTrue(state.isSaveButtonEnabled)
    }

    @Test
    fun `edit mode should disable save when invalid`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onNameChanged("")

        val state = viewModel.awaitState { !it.isSaveButtonEnabled }

        assertFalse(state.isSaveButtonEnabled)
    }

    // endregion

    // region Events - Save

    @Test
    fun `onSaveClick should emit NavigateBack when valid`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onNameChanged("Food")
        viewModel.onIconSelected(IconPack.FOOD)
        viewModel.onCategoryChanged(1)

        advanceUntilIdle()

        viewModel.events.test {
            viewModel.onSaveClick()
            assertEquals(SubcategoryEditorEvent.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `onSaveClick should not emit event if invalid`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.events.test {
            viewModel.onSaveClick()
            expectNoEvents()
        }
    }

    // endregion

    // region Delete Flow

    @Test
    fun `delete should navigate back on success`() = runTest {
        coEvery { deleteSubcategory(any()) } returns Unit

        val viewModel = createViewModelEditMode()

        viewModel.events.test {
            viewModel.onDeleteDialogAction(ConfirmationDialogAction.Confirm)
            assertEquals(SubcategoryEditorEvent.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `delete should emit snackbar on failure`() = runTest {
        coEvery { deleteSubcategory(any()) } throws RuntimeException()

        val viewModel = createViewModelEditMode()

        viewModel.events.test {
            viewModel.onDeleteDialogAction(ConfirmationDialogAction.Confirm)
            assertTrue(awaitItem() is SubcategoryEditorEvent.ShowSnackBar)
        }
    }

    @Test
    fun `delete should set loading state`() = runTest {
        coEvery { deleteSubcategory(any()) } coAnswers {
            delay(100)
        }

        val viewModel = createViewModelEditMode()

        viewModel.onDeleteDialogAction(ConfirmationDialogAction.Confirm)

        val state = viewModel.awaitState { it.isDeleting }

        assertTrue(state.isDeleting)
    }

    // endregion

    // region Dialogs

    @Test
    fun `onCloseClick should open discard dialog when unsaved changes`() = runTest {
        val viewModel = createViewModelEditMode()

        advanceUntilIdle()
        viewModel.onNameChanged("Changed")
        advanceUntilIdle()
        viewModel.onCloseClick()

        val state = viewModel.awaitState { it.discardDialog.visible }

        assertTrue(state.discardDialog.visible)
    }

    @Test
    fun `confirm discard should navigate back`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.events.test {
            viewModel.onDiscardDialogAction(ConfirmationDialogAction.Confirm)
            assertEquals(SubcategoryEditorEvent.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `icon selector apply should update icon`() = runTest {
        val viewModel = createViewModelCreateMode()

        val icon = IconPack.FOOD

        viewModel.onIconSelectorAction(SelectorDialogAction.Open)
        viewModel.onIconSelectorAction(SelectorDialogAction.Select(icon))
        viewModel.onIconSelectorAction(SelectorDialogAction.Apply)

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(icon, state.subcategory.icon)
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
        createViewModel(SubcategoryEditorConfig(null, 1))

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

    private suspend fun SubcategoryEditorViewModel.awaitState(
        predicate: (SubcategoryEditorUiState) -> Boolean
    ): SubcategoryEditorUiState {
        return uiState.first(predicate)
    }

    private suspend fun <T> Flow<T>.awaitSecondItem(): T {
        var result: T? = null
        test {
            awaitItem()
            result = awaitItem()
        }
        return result ?: error("No item emitted")
    }

    private fun encodeConfig(config: SubcategoryEditorConfig): String {
        return URLEncoder.encode(Json.encodeToString(config), "UTF-8")
    }

    // endregion
}