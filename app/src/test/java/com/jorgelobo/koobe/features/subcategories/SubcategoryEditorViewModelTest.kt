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
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorViewModel
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
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.URLEncoder

@OptIn(ExperimentalCoroutinesApi::class)
class SubcategoryEditorViewModelTest {

    private val subcategoryRepository: SubcategoryRepository = mockk()
    private val categoryRepository: CategoryRepository = mockk()

    private lateinit var savedStateHandle: SavedStateHandle

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

        val category = Category(
            id = 1,
            name = "Food",
            icon = IconPack.FOOD.icon,
            color = "FF00FF",
            type = TransactionType.EXPENSE
        )

        every { categoryRepository.getCategoryByIdFlow(1) } returns flowOf(category)

        val config = Json.encodeToString(
            SubcategoryEditorConfig(
                subcategoryId = null,
                categoryId = 1
            )
        )

        val savedStateHandle = SavedStateHandle(
            mapOf("config" to config)
        )

        val viewModel = SubcategoryEditorViewModel(
            savedStateHandle,
            subcategoryRepository,
            categoryRepository
        )

        viewModel.uiState.test {

            // First emission = initialEmpty()
            awaitItem()

            // Second emission = real state
            val state = awaitItem()

            assertEquals("", state.subcategory.name)
            assertEquals(1, state.category.id)
        }
    }

    // Test case for edit mode
    @Test
    fun `edit mode should load subcategory from flow`() = runTest {

        val subcategory = Subcategory(
            id = 2,
            name = "Groceries",
            icon = IconPack.FOOD.icon,
            categoryId = 1
        )

        val category = Category(
            id = 1,
            name = "Food",
            icon = IconPack.FOOD.icon,
            color = "FF00FF",
            type = TransactionType.EXPENSE
        )

        every { subcategoryRepository.getSubcategoryByIdFlow(2) } returns flowOf(subcategory)
        every { categoryRepository.getCategoryByIdFlow(1) } returns flowOf(category)

        val config = Json.encodeToString(
            SubcategoryEditorConfig(
                subcategoryId = 2,
                categoryId = 1
            )
        )

        val savedStateHandle = SavedStateHandle(
            mapOf("config" to config)
        )

        val viewModel = SubcategoryEditorViewModel(
            savedStateHandle,
            subcategoryRepository,
            categoryRepository
        )

        viewModel.uiState.test {

            // First emission = initialEmpty()
            awaitItem()

            // Second emission = real state
            val state = awaitItem()

            assertEquals("Groceries", state.subcategory.name)
            assertEquals(1, state.category.id)
        }
    }

    @Test
    fun `uiState should update when flow emits new value`() = runTest {

        val flow = MutableStateFlow<Subcategory?>(null)

        val category = Category(
            id = 1,
            name = "Food",
            icon = IconPack.FOOD.icon,
            color = "FF00FF",
            type = TransactionType.EXPENSE
        )

        every { subcategoryRepository.getSubcategoryByIdFlow(2) } returns flow
        every { categoryRepository.getCategoryByIdFlow(1) } returns flowOf(category)

        savedStateHandle = SavedStateHandle(
            mapOf(
                "config" to URLEncoder.encode(
                    Json.encodeToString(
                        SubcategoryEditorConfig(
                            subcategoryId = 2,
                            categoryId = 1
                        )
                    ),
                    "UTF-8"
                )
            )
        )

        val viewModel = SubcategoryEditorViewModel(
            savedStateHandle,
            subcategoryRepository,
            categoryRepository
        )

        viewModel.uiState.test {

            // First emission = initialEmpty()
            awaitItem()

            // Second emission = real state
            val initial = awaitItem()
            assertEquals("", initial.subcategory.name)

            flow.value = Subcategory(
                id = 2,
                name = "Updated Name",
                icon = IconPack.FOOD.icon,
                categoryId = 1
            )

            advanceUntilIdle()

            val updated = awaitItem()
            assertEquals("Updated Name", updated.subcategory.name)
        }
    }
}