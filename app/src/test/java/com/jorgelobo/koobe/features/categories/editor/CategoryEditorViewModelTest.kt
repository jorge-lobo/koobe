package com.jorgelobo.koobe.features.categories.editor

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorIntent
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import java.net.URLEncoder
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryEditorViewModelTest {

    private val categoryRepository: CategoryRepository = mockk()

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

        val viewModel = createViewModel(
            config = CategoryEditorConfig(null),
            categoryFlow = flowOf(Category.empty())
        )

        val state = viewModel.uiState.awaitSecondItem()

        assertEquals("", state.category.name)
    }

    @Test
    fun `edit mode should emit category from flow`() = runTest {

        val viewModel = createViewModel(
            config = CategoryEditorConfig(1),
            categoryFlow = flowOf(fakeCategory())
        )

        val state = viewModel.uiState.first { it.category.name == "Food" }

        assertEquals("Food", state.category.name)
    }

    @Test
    fun `name change intent should update category name`() = runTest {

        val viewModel = createViewModel(config = CategoryEditorConfig(null))

        viewModel.onIntent(CategoryEditorIntent.State.NameChanged("New Name"))

        val state = viewModel.uiState.first { it.category.name == "New Name" }

        assertEquals("New Name", state.category.name)
    }


    // endregion

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
            categoryRepository = categoryRepository
        )
    }

    private fun fakeCategory(id: Int = 1) = Category(
        id = id,
        name = "Food",
        icon = IconPack.FOOD,
        color = "FF00FF",
        type = TransactionType.EXPENSE,
        subcategories = emptyList()
    )

    private suspend fun <T> Flow<T>.awaitSecondItem(): T {
        var result: T? = null
        test {
            awaitItem()
            result = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        return result ?: error("No item emitted")
    }

    private fun encodeConfig(config: CategoryEditorConfig): String {
        return URLEncoder.encode(Json.encodeToString(config), "UTF-8")
    }

    // endregion
}