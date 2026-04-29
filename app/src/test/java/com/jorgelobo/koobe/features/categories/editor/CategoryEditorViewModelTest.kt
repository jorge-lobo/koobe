package com.jorgelobo.koobe.features.categories.editor

import androidx.lifecycle.SavedStateHandle
import com.jorgelobo.koobe.common.extensions.toColor
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorIntent
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryEditorViewModelTest {

    private val categoryRepository: CategoryRepository = mockk()
    private val subCategoryRepository: SubcategoryRepository = mockk()

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
        viewModel.onIntent(CategoryEditorIntent.State.ColorSelected("#FF00FF".toColor()))
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
        viewModel.onIntent(CategoryEditorIntent.State.ColorSelected("#FF00FF".toColor()))

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertFalse(state.isSaveEnabled)
    }

    @Test
    fun `create mode should disable save when color is blank`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("New Name"))
        viewModel.onIntent(CategoryEditorIntent.State.IconSelected(IconPack.FOOD))

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

    // region Validation - Edge Cases

    @Test
    fun `save should remain disabled until all required fields are valid`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("New Name"))
        advanceUntilIdle()
        val nameUpdatedState = viewModel.uiState.value

        assertFalse(nameUpdatedState.isSaveEnabled)

        viewModel.onIntent(CategoryEditorIntent.State.IconSelected(IconPack.FOOD))
        advanceUntilIdle()
        val iconUpdatedState = viewModel.uiState.value

        assertFalse(iconUpdatedState.isSaveEnabled)

        viewModel.onIntent(CategoryEditorIntent.State.ColorSelected("#FF00FF".toColor()))
        advanceUntilIdle()
        val colorUpdatedState = viewModel.uiState.value

        assertTrue(colorUpdatedState.isSaveEnabled)
    }

    // end region

    // region Helpers

    private fun createViewModel(
        config: CategoryEditorConfig,
        categoryFlow: Flow<Category> = flowOf(Category.empty())
    ): CategoryEditorViewModel {

        every { categoryRepository.getCategoryByIdFlow(1) } returns categoryFlow

        val savedStateHandle = SavedStateHandle(
            mapOf("config" to encodeConfig(config))
        )

        return CategoryEditorViewModel(
            savedStateHandle = savedStateHandle,
            categoryRepository = categoryRepository,
            subcategoryRepository = subCategoryRepository,
            saveCategory = mockk(),
            deleteCategoryWithReassign = mockk(),
            deleteSubcategoryWithReassign = mockk()
        )
    }

    private fun createViewModelCreateMode() =
        createViewModel(CategoryEditorConfig(null))

    private fun createViewModelEditMode() =
        createViewModel(
            config = CategoryEditorConfig(1),
            categoryFlow = flowOf(fakeCategory())
        )

    private fun fakeCategory(id: Int = 1) = Category(
        id = id,
        name = "Food",
        icon = IconPack.FOOD,
        color = "FF00FF",
        type = TransactionType.EXPENSE,
        subcategories = emptyList()
    )

    private fun encodeConfig(config: CategoryEditorConfig): String {
        return URLEncoder.encode(Json.encodeToString(config), "UTF-8")
    }

    // endregion
}