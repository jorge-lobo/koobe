package com.jorgelobo.koobe.ui.screen.subcategories

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class SubcategoryEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    subcategoryRepository: SubcategoryRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _events = MutableSharedFlow<SubcategoryEditorEvent>()
    val events = _events.asSharedFlow()

    private val config: SubcategoryEditorConfig =
        savedStateHandle.get<String>("config")
            ?.let { URLDecoder.decode(it, "UTF-8") }
            ?.let { Json.decodeFromString<SubcategoryEditorConfig>(it) }
            ?: error("Missing SubcategoryEditorConfig")

    private val subcategoryFlow: Flow<Subcategory?> =
        if (config.isEditMode) {
            subcategoryRepository.getSubcategoryByIdFlow(config.subcategoryId!!)
        } else {
            flowOf(null)
        }

    private val categoryFlow: Flow<Category?> =
        config.categoryId
            ?.let { categoryRepository.getCategoryByIdFlow(it) }
            ?: flowOf(null)

    private val userInput = MutableStateFlow(SubcategoryInputState())

    private val baseStateFlow: Flow<SubcategoryEditorUiState> =
        combine(
            subcategoryFlow,
            categoryFlow
        ) { subcategory, category ->

            val safeCategory = category ?: Category.empty()

            if (config.isEditMode) {
                if (subcategory == null) {
                    SubcategoryEditorUiState.initialEmpty().copy(
                        errorMessage = "Subcategory not found"
                    )
                } else {
                    SubcategoryEditorUiState.initial(
                        config = config,
                        category = safeCategory,
                        subcategory = subcategory
                    )
                }
            } else {
                SubcategoryEditorUiState.initial(
                    config = config,
                    category = safeCategory,
                    subcategory = Subcategory.empty()
                )
            }
        }

    val uiState: StateFlow<SubcategoryEditorUiState> =
        combine(
            baseStateFlow,
            userInput
        ) { base, input ->

            val updatedSubcategory = base.subcategory.copy(
                name = input.name ?: base.subcategory.name,
                icon = input.icon ?: base.subcategory.icon,
                categoryId = input.categoryId ?: base.subcategory.categoryId
            )

            base.copy(
                subcategory = updatedSubcategory
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = SubcategoryEditorUiState.initialEmpty()
            )

    fun onNameChanged(name: String) {
        userInput.update { it.copy(name = name) }
    }

    fun onIconSelected(icon: ImageVector) {
        userInput.update { it.copy(icon = icon) }
    }

    fun onCategoryChanged(id: Int) {
        userInput.update { it.copy(categoryId = id) }
    }

}