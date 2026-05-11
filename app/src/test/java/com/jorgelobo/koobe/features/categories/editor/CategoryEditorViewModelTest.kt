package com.jorgelobo.koobe.features.categories.editor

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.jorgelobo.koobe.common.extensions.toColor
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.domain.usecase.category.DeleteCategoryWithReassignUseCase
import com.jorgelobo.koobe.domain.usecase.category.SaveCategoryUseCase
import com.jorgelobo.koobe.domain.usecase.subcategory.DeleteSubcategoryWithReassignUseCase
import com.jorgelobo.koobe.domain.validation.NameValidationException
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorDeleteTarget
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorEvent
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorIntent
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorViewModel
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryEditorViewModelTest {

    private val categoryRepository: CategoryRepository = mockk()
    private val subCategoryRepository: SubcategoryRepository = mockk()
    private val saveCategory: SaveCategoryUseCase = mockk()
    private val deleteCategoryWithReassign: DeleteCategoryWithReassignUseCase = mockk()
    private val deleteSubcategoryWithReassign: DeleteSubcategoryWithReassignUseCase = mockk()

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
    fun `create mode should initialize with empty category`() = runTest {
        val viewModel = createViewModelCreateMode()

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals("", state.category.name)
    }

    @Test
    fun `edit mode should emit category from flow`() = runTest {
        val viewModel = createViewModelEditMode()

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals("Food", state.category.name)
    }

    @Test
    fun `name change intent should update category name`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("New Name"))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals("New Name", state.category.name)
    }

    // endregion

    // region Validation - Create Mode

    @Test
    fun `create mode should start with save disabled`() = runTest {
        val viewModel = createViewModelCreateMode()

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertFalse(state.isSaveEnabled)
    }

    @Test
    fun `create mode should enable save when valid`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("New Name"))
        viewModel.onIntent(CategoryEditorIntent.State.IconSelected(IconPack.FOOD))
        viewModel.onIntent(CategoryEditorIntent.State.ColorSelected("#ffff00ff".toColor()))
        viewModel.onIntent(CategoryEditorIntent.State.TypeSelected(TransactionType.EXPENSE))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertTrue(state.isSaveEnabled)
    }

    @Test
    fun `create mode should disable save when name is blank`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged(""))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertFalse(state.isSaveEnabled)
    }

    @Test
    fun `create mode should disable save when icon is placeholder`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("New Name"))
        viewModel.onIntent(CategoryEditorIntent.State.ColorSelected("#ffff00ff".toColor()))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertFalse(state.isSaveEnabled)
    }

    // endregion

    // region Validation - Edit Mode

    @Test
    fun `edit mode should keep save disabled when unchanged`() = runTest {
        val viewModel = createViewModelEditMode()

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertFalse(state.isSaveEnabled)
    }

    @Test
    fun `edit mode should enable save only after change`() = runTest {
        val viewModel = createViewModelEditMode()
        val initialState = viewModel.uiState.value

        assertFalse(initialState.isSaveEnabled)

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("New Name"))

        advanceUntilIdle()
        val updatedState = viewModel.uiState.value

        assertTrue(updatedState.isSaveEnabled)
    }

    @Test
    fun `edit mode should disable save when invalid`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged(""))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertFalse(state.isSaveEnabled)
    }

    @Test
    fun `edit mode should disable save when value is reverted`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("New Name"))

        advanceUntilIdle()
        val initialState = viewModel.uiState.value

        assertTrue(initialState.isSaveEnabled)

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("Food"))

        advanceUntilIdle()
        val updatedState = viewModel.uiState.value

        assertFalse(updatedState.isSaveEnabled)
    }

    // endregion

    // region Save

    @Test
    fun `save clicked should call save use case`() = runTest {
        coEvery { saveCategory(any(), any()) } just runs

        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("Food"))
        viewModel.onIntent(CategoryEditorIntent.State.IconSelected(IconPack.FOOD))
        viewModel.onIntent(CategoryEditorIntent.State.ColorSelected("#ffff00ff".toColor()))
        viewModel.onIntent(CategoryEditorIntent.State.TypeSelected(TransactionType.EXPENSE))

        advanceUntilIdle()
        viewModel.onIntent(CategoryEditorIntent.Action.SaveClicked)

        advanceUntilIdle()
        coVerify(exactly = 1) {
            saveCategory(
                category = match {
                    it.name == "Food" &&
                            it.icon == IconPack.FOOD &&
                            it.color == "#ffff00ff" &&
                            it.type == TransactionType.EXPENSE
                },
                isEditMode = false
            )
        }
    }

    @Test
    fun `successful save should emit navigate back event`() = runTest {
        coEvery { saveCategory(any(), any()) } just runs

        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("Food"))
        viewModel.onIntent(CategoryEditorIntent.State.IconSelected(IconPack.FOOD))
        viewModel.onIntent(CategoryEditorIntent.State.ColorSelected("#ffff00ff".toColor()))
        viewModel.onIntent(CategoryEditorIntent.State.TypeSelected(TransactionType.EXPENSE))

        advanceUntilIdle()
        val events = mutableListOf<CategoryEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(CategoryEditorIntent.Action.SaveClicked)

        advanceUntilIdle()
        assertTrue(events.contains(CategoryEditorEvent.NavigateBack))
        job.cancel()
    }

    @Test
    fun `invalid save should emit snackbar event`() = runTest {
        val viewModel = createViewModelCreateMode()
        viewModel.events.test {
            viewModel.onIntent(CategoryEditorIntent.State.NameChanged(""))

            advanceUntilIdle()
            viewModel.onIntent(CategoryEditorIntent.Action.SaveClicked)

            advanceUntilIdle()
            assertTrue(awaitItem() is CategoryEditorEvent.ShowSnackbar)
        }
    }

    @Test
    fun `save failure should emit snackbar event`() = runTest {
        coEvery { saveCategory(any(), any()) } throws RuntimeException()

        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("Food"))
        viewModel.onIntent(CategoryEditorIntent.State.IconSelected(IconPack.FOOD))
        viewModel.onIntent(CategoryEditorIntent.State.ColorSelected("#ffff00ff".toColor()))
        viewModel.onIntent(CategoryEditorIntent.State.TypeSelected(TransactionType.EXPENSE))

        advanceUntilIdle()
        val events = mutableListOf<CategoryEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(CategoryEditorIntent.Action.SaveClicked)

        advanceUntilIdle()
        assertTrue(events.any { it is CategoryEditorEvent.ShowSnackbar })
        job.cancel()
    }

    @Test
    fun `name validation exception should update error state`() = runTest {
        val exception = NameValidationException.TooShort()

        coEvery { saveCategory(any(), any()) } throws exception

        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("A"))
        viewModel.onIntent(CategoryEditorIntent.State.IconSelected(IconPack.FOOD))
        viewModel.onIntent(CategoryEditorIntent.State.ColorSelected("#ffff00ff".toColor()))
        viewModel.onIntent(CategoryEditorIntent.State.TypeSelected(TransactionType.EXPENSE))

        advanceUntilIdle()
        viewModel.onIntent(CategoryEditorIntent.Action.SaveClicked)

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals(exception, state.nameError)
        assertEquals(InputState.ERROR, state.nameInputState)
    }

    @Test
    fun `name changed should clear validation error`() = runTest {
        val exception = NameValidationException.TooShort()

        coEvery { saveCategory(any(), any()) } throws exception

        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("A"))
        viewModel.onIntent(CategoryEditorIntent.State.IconSelected(IconPack.FOOD))
        viewModel.onIntent(CategoryEditorIntent.State.ColorSelected("#ffff00ff".toColor()))
        viewModel.onIntent(CategoryEditorIntent.State.TypeSelected(TransactionType.EXPENSE))

        advanceUntilIdle()
        viewModel.onIntent(CategoryEditorIntent.Action.SaveClicked)

        advanceUntilIdle()
        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("New Name"))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertNull(state.nameError)
        assertEquals(InputState.DEFAULT, state.nameInputState)
    }

    // endregion

    // region Delete Category

    @Test
    fun `request delete category should open delete dialog`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onIntent(CategoryEditorIntent.Action.RequestDeleteCategory)

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertTrue(state.deleteDialog.visible)
        assertEquals(CategoryEditorDeleteTarget.Category, state.deleteTarget)
    }

    @Test
    fun `confirm delete category should call delete use case`() = runTest {
        coEvery { deleteCategoryWithReassign(any()) } just runs

        val viewModel = createViewModelEditMode()

        viewModel.onIntent(CategoryEditorIntent.Action.RequestDeleteCategory)

        advanceUntilIdle()
        viewModel.onIntent(
            CategoryEditorIntent.Action.DeleteDialogAction(
                ConfirmationDialogAction.Confirm
            )
        )

        advanceUntilIdle()
        coVerify(exactly = 1) { deleteCategoryWithReassign(match { it.name == "Food" }) }
    }

    @Test
    fun `successful delete category should navigate back`() = runTest {
        coEvery { deleteCategoryWithReassign(any()) } just runs

        val viewModel = createViewModelEditMode()
        val events = mutableListOf<CategoryEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(CategoryEditorIntent.Action.RequestDeleteCategory)

        advanceUntilIdle()
        viewModel.onIntent(
            CategoryEditorIntent.Action.DeleteDialogAction(
                ConfirmationDialogAction.Confirm
            )
        )

        advanceUntilIdle()
        assertTrue(events.contains(CategoryEditorEvent.NavigateBack))
        job.cancel()
    }

    @Test
    fun `delete category failure should emit snackbar`() = runTest {
        coEvery { deleteCategoryWithReassign(any()) } throws RuntimeException()

        val viewModel = createViewModelEditMode()
        val events = mutableListOf<CategoryEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(CategoryEditorIntent.Action.RequestDeleteCategory)

        advanceUntilIdle()
        viewModel.onIntent(
            CategoryEditorIntent.Action.DeleteDialogAction(
                ConfirmationDialogAction.Confirm
            )
        )

        advanceUntilIdle()
        assertTrue(events.any { it is CategoryEditorEvent.ShowSnackbar })
        job.cancel()
    }

    // endregion

    // region Delete Subcategory

    @Test
    fun `request delete subcategory should open delete dialog`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onIntent(CategoryEditorIntent.Action.RequestDeleteSubcategory(10))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertTrue(state.deleteDialog.visible)
        assertEquals(CategoryEditorDeleteTarget.Subcategory(10), state.deleteTarget)
    }

    @Test
    fun `confirm delete subcategory should call use case`() = runTest {
        coEvery { deleteSubcategoryWithReassign(any()) } just runs

        val viewModel = createViewModelEditModeWithSubcategories()

        viewModel.onIntent(CategoryEditorIntent.Action.RequestDeleteSubcategory(10))

        advanceUntilIdle()
        viewModel.onIntent(
            CategoryEditorIntent.Action.DeleteDialogAction(
                ConfirmationDialogAction.Confirm
            )
        )

        advanceUntilIdle()
        coVerify(exactly = 1) { deleteSubcategoryWithReassign(match { it.id == 10 }) }
    }

    @Test
    fun `successful subcategory delete should remove subcategory from state`() = runTest {
        coEvery { deleteSubcategoryWithReassign(any()) } just runs

        val viewModel = createViewModelEditModeWithSubcategories()

        viewModel.onIntent(CategoryEditorIntent.Action.RequestDeleteSubcategory(10))

        advanceUntilIdle()
        viewModel.onIntent(
            CategoryEditorIntent.Action.DeleteDialogAction(
                ConfirmationDialogAction.Confirm
            )
        )

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertTrue(state.category.subcategories.none { it.id == 10 })
    }

    // endregion

    // region Dialogs

    @Test
    fun `close clicked with changes should open discard dialog`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("Food"))

        advanceUntilIdle()
        viewModel.onIntent(CategoryEditorIntent.Action.CloseClicked)

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertTrue(state.discardDialog.visible)
    }

    @Test
    fun `confirm discard should navigate back`() = runTest {
        val viewModel = createViewModelCreateMode()
        val events = mutableListOf<CategoryEditorEvent>()
        val job = launch { viewModel.events.toList(events) }

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("Food"))

        advanceUntilIdle()
        viewModel.onIntent(CategoryEditorIntent.Action.CloseClicked)

        advanceUntilIdle()
        viewModel.onIntent(
            CategoryEditorIntent.Action.DiscardDialogAction(
                ConfirmationDialogAction.Confirm
            )
        )

        advanceUntilIdle()
        assertTrue(events.contains(CategoryEditorEvent.NavigateBack))
        job.cancel()
    }

    @Test
    fun `applying icon selector should update category icon`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(
            CategoryEditorIntent.Action.IconSelectorDialogAction(
                SelectorDialogAction.Select(IconPack.FOOD)
            )
        )
        viewModel.onIntent(
            CategoryEditorIntent.Action.IconSelectorDialogAction(
                SelectorDialogAction.Apply
            )
        )

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals(IconPack.FOOD, state.category.icon)
    }

    @Test
    fun `applying color selector should update category color`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onIntent(
            CategoryEditorIntent.Action.ColorSelectorDialogAction(
                SelectorDialogAction.Select("#ffff00ff".toColor())
            )
        )
        viewModel.onIntent(
            CategoryEditorIntent.Action.IconSelectorDialogAction(
                SelectorDialogAction.Apply
            )
        )

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals("#ffff00ff", state.category.color)
    }

    // endregion

    // region Snapshot

    @Test
    fun `initial snapshot should remain unchanged after edits`() = runTest {
        val viewModel = createViewModelEditMode()

        advanceUntilIdle()
        val initialSnapshot = viewModel.uiState.value.initialSnapshot

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("New Name"))

        advanceUntilIdle()
        val updatedSnapshot = viewModel.uiState.value.initialSnapshot

        assertEquals(initialSnapshot, updatedSnapshot)
    }

    // endregion

    // region Helpers

    private fun createViewModel(
        config: CategoryEditorConfig,
        categoryFlow: Flow<Category> = flowOf(Category.empty()),
        subcategoriesFlow: Flow<List<Subcategory>> = flowOf(emptyList())
    ): CategoryEditorViewModel {

        every { categoryRepository.getCategoryByIdFlow(1) } returns categoryFlow
        every { subCategoryRepository.getSubcategoriesByCategoryId(1) } returns subcategoriesFlow

        val savedStateHandle = SavedStateHandle(
            mapOf("config" to encodeConfig(config))
        )

        return CategoryEditorViewModel(
            savedStateHandle = savedStateHandle,
            categoryRepository = categoryRepository,
            subcategoryRepository = subCategoryRepository,
            saveCategory = saveCategory,
            deleteCategoryWithReassign = deleteCategoryWithReassign,
            deleteSubcategoryWithReassign = deleteSubcategoryWithReassign
        )
    }

    private fun createViewModelCreateMode() =
        createViewModel(CategoryEditorConfig(null))

    private fun createViewModelEditMode() =
        createViewModel(
            config = CategoryEditorConfig(1),
            categoryFlow = flowOf(fakeCategory())
        )

    private fun createViewModelEditModeWithSubcategories() =
        createViewModel(
            config = CategoryEditorConfig(1),
            categoryFlow = flowOf(fakeCategory()),
            subcategoriesFlow = flowOf(
                listOf(
                    fakeSubcategory(10),
                    fakeSubcategory(20)
                )
            )
        )

    private fun fakeCategory(id: Int = 1) = Category(
        id = id,
        name = "Food",
        icon = IconPack.FOOD,
        color = "#ffff00ff",
        type = TransactionType.EXPENSE,
        subcategories = emptyList()
    )

    private fun fakeSubcategory(id: Int) = Subcategory(
        id = id,
        categoryId = 1,
        name = "Subcategory $id",
        icon = IconPack.FOOD
    )

    private fun encodeConfig(config: CategoryEditorConfig): String {
        return URLEncoder.encode(Json.encodeToString(config), "UTF-8")
    }

    // endregion
}