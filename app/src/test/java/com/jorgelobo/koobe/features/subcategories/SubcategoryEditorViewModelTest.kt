package com.jorgelobo.koobe.features.subcategories

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorUiState
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
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

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Test case for create mode
    @Test
    fun `create mode should emit empty subcategory and category from flow`() = runTest {

        val category = fakeCategory()

        val viewModel = createViewModel(
            SubcategoryEditorConfig(
                subcategoryId = null,
                categoryId = 1
            ),
            subcategoryFlow = flowOf(null),
            categoryFlow = flowOf(category)
        )

        val state = viewModel.uiState.awaitSecondItem()

        assertEquals("", state.subcategory.name)
        assertEquals(1, state.category.id)

    }

    // Test case for edit mode
    @Test
    fun `edit mode should load subcategory from flow`() = runTest {

        val subcategory = fakeSubcategory()
        val category = fakeCategory()

        val viewModel = createViewModel(
            SubcategoryEditorConfig(
                subcategoryId = 2,
                categoryId = 1
            ),
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
            SubcategoryEditorConfig(
                subcategoryId = 2,
                categoryId = 1
            ),
            subcategoryFlow = flow,
            categoryFlow = flowOf(category)
        )

        viewModel.uiState.test {

            skipItems(1)
            val initial = awaitItem()
            assertEquals("", initial.subcategory.name)

            flow.value = Subcategory(
                id = 2,
                name = "Updated Name",
                icon = IconPack.FOOD.icon,
                categoryId = 1
            )

            val updated = awaitItem()
            assertEquals("Updated Name", updated.subcategory.name)
        }
    }

    @Test
    fun `onNameChanged should update subcategory name`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onNameChanged("New Name")

        val state = viewModel.awaitState {
            it.subcategory.name == "New Name"
        }

        assertEquals("New Name", state.subcategory.name)
    }

    @Test
    fun `onResetName should reset subcategory name`() = runTest {

        val flow = MutableStateFlow<Subcategory?>(
            fakeSubcategory(name = "Initial Name")
        )

        val category = fakeCategory()

        val viewModel = createViewModel(
            SubcategoryEditorConfig(
                subcategoryId = 2,
                categoryId = 1
            ),
            subcategoryFlow = flow,
            categoryFlow = flowOf(category)
        )

        val initial = viewModel.awaitState {
            it.subcategory.name == "Initial Name"
        }

        viewModel.onResetName()

        val updated = viewModel.awaitState {
            it.subcategory.name == ""
        }

        assertEquals("Initial Name", initial.subcategory.name)
        assertEquals("", updated.subcategory.name)
    }

    @Test
    fun `onIconSelected should update icon`() = runTest {
        val viewModel = createViewModelCreateMode()

        val icon = IconPack.FOOD.icon
        viewModel.onIconSelected(icon)

        val state = viewModel.awaitState {
            it.subcategory.icon == icon
        }

        assertEquals(icon, state.subcategory.icon)
    }

    @Test
    fun `onCategoryChanged should update categoryId`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onCategoryChanged(5)

        val state = viewModel.awaitState {
            it.subcategory.categoryId == 5
        }

        assertEquals(5, state.subcategory.categoryId)
    }

    @Test
    fun `create mode should enable save when subcategory is valid`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onNameChanged("Food")
        viewModel.onIconSelected(IconPack.FOOD.icon)

        val state = viewModel.awaitState {
            it.isSaveButtonEnabled
        }

        assertTrue(state.isSaveButtonEnabled)
    }

    @Test
    fun `create mode should disable save when name is blank`() = runTest {
        val viewModel = createViewModelCreateMode()

        viewModel.onNameChanged("")
        viewModel.onIconSelected(IconPack.FOOD.icon)

        val state = viewModel.awaitState {
            !it.isSaveButtonEnabled
        }

        assertFalse(state.isSaveButtonEnabled)
    }

    @Test
    fun `edit mode should enable save when valid change occurs`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onNameChanged("New Name")

        val state = viewModel.awaitState {
            it.isSaveButtonEnabled
        }

        assertTrue(state.isSaveButtonEnabled)
    }

    @Test
    fun `edit mode should disable save when name is blank`() = runTest {
        val viewModel = createViewModelEditMode()

        viewModel.onNameChanged("")

        val state = viewModel.awaitState {
            !it.isSaveButtonEnabled
        }

        assertFalse(state.isSaveButtonEnabled)
    }

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
            categoryRepository
        )
    }

    private fun createViewModelCreateMode(): SubcategoryEditorViewModel {
        return createViewModel(
            SubcategoryEditorConfig(
                subcategoryId = null,
                categoryId = 1
            )
        )
    }

    private fun createViewModelEditMode(
        subcategory: Subcategory = fakeSubcategory()
    ): SubcategoryEditorViewModel {
        return createViewModel(
            SubcategoryEditorConfig(
                subcategoryId = subcategory.id,
                categoryId = subcategory.categoryId
            ),
            subcategoryFlow = flowOf(subcategory),
            categoryFlow = flowOf(fakeCategory(subcategory.categoryId))
        )
    }

    private fun fakeCategory(id: Int = 1) = Category(
        id = id,
        name = "Food",
        icon = IconPack.FOOD.icon,
        color = "FF00FF",
        type = TransactionType.EXPENSE
    )

    private fun fakeSubcategory(name: String = "Groceries") = Subcategory(
        id = 2,
        name = name,
        icon = IconPack.FOOD.icon,
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
        return URLEncoder.encode(
            Json.encodeToString(config),
            "UTF-8"
        )
    }
}