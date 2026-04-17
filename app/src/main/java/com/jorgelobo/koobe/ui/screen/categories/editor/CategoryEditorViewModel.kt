package com.jorgelobo.koobe.ui.screen.categories.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.core.model.resolve
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.ui.screen.categories.editor.state.CategoryFormState
import com.jorgelobo.koobe.ui.screen.categories.editor.state.CategoryUiStateInternal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class CategoryEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    categoryRepository: CategoryRepository
) : ViewModel() {

    private val _events = MutableSharedFlow<CategoryEditorEvent>()
    val events = _events.asSharedFlow()

    private val config: CategoryEditorConfig =
        savedStateHandle.get<String>("config")
            ?.let { URLDecoder.decode(it, "UTF-8") }
            ?.let { Json.decodeFromString<CategoryEditorConfig>(it) }
            ?: error("Missing CategoryEditorConfig")

    private val categoryFlow: Flow<Category> =
        if (config.isEditMode) {
            categoryRepository.getCategoryByIdFlow(config.categoryId!!)
                .map { it ?: error("Category not found") }
        } else {
            flowOf(Category.empty())
        }

    private val formState = MutableStateFlow(CategoryFormState())
    private val uiInternalState = MutableStateFlow(CategoryUiStateInternal())

    private val baseStateFlow: Flow<CategoryEditorUiState> =
        categoryFlow.map { category ->

            CategoryEditorUiState.initial(
                config = config,
                category = category
            )
        }

    val uiState: StateFlow<CategoryEditorUiState> =
        combine(
            baseStateFlow,
            formState,
            uiInternalState
        ) { base, form, uiInternal ->

            val updatedCategory = base.category.copy(
                name = form.name.resolve(base.category.name),
                icon = form.icon.resolve(base.category.icon),
                color = form.color.resolve(base.category.color),
                type = form.type.resolve(base.category.type),
                subcategories = form.subcategories.resolve(base.category.subcategories)
            )

            base.copy(
                category = updatedCategory,
                discardDialog = uiInternal.discardDialog,
                deleteDialog = uiInternal.deleteDialog,
                iconDialog = uiInternal.iconSelectorDialog,
                colorDialog = uiInternal.colorSelectorDialog,
                infoDialog = uiInternal.infoDialog,
                isDeleting = uiInternal.isDeleting,
                isSaveButtonEnabled = computeSaveEnabled(
                    base.copy(category = updatedCategory)
                )
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = CategoryEditorUiState.initialEmpty()
            )

    private fun computeSaveEnabled(state: CategoryEditorUiState): Boolean {
        val isValid = state.isValid

        return when {
            !isValid -> false
            config.isEditMode -> state.hasUnsavedChanges
            else -> true
        }
    }

    fun onIntent(intent: CategoryEditorIntent) {
        when (intent) {

            is CategoryEditorIntent.Action -> handleAction(intent)

            is CategoryEditorIntent.State -> {
                val result = CategoryEditorReducer.reduce(
                    intent = intent,
                    currentForm = formState.value,
                    currentInternal = uiInternalState.value
                )

                formState.value = result.form
                uiInternalState.value = result.internal
            }
        }
    }

    private fun handleAction(intent: CategoryEditorIntent.Action) {
        when (intent) {
            CategoryEditorIntent.Action.SaveClicked -> handleSave()
            CategoryEditorIntent.Action.DeleteClicked -> handleDelete()
            CategoryEditorIntent.Action.BackClicked -> handleBack()
        }
    }

    private fun handleSave() {}
    private fun handleDelete() {}
    private fun handleBack() {}
}