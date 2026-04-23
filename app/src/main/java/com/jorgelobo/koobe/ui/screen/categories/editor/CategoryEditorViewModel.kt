package com.jorgelobo.koobe.ui.screen.categories.editor

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.core.model.resolve
import com.jorgelobo.koobe.core.model.resolveToHex
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.domain.usecase.category.DeleteCategoryWithReassignUseCase
import com.jorgelobo.koobe.domain.usecase.category.SaveCategoryUseCase
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.categories.editor.state.CategoryFormState
import com.jorgelobo.koobe.ui.screen.categories.editor.state.CategoryUiStateInternal
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.reduceConfirmationDialog
import com.jorgelobo.koobe.ui.screen.common.dialog.info.InfoDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.reduceSelectorDialog
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class CategoryEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    categoryRepository: CategoryRepository,
    subcategoryRepository: SubcategoryRepository,
    private val saveCategory: SaveCategoryUseCase,
    private val deleteCategoryWithReassign: DeleteCategoryWithReassignUseCase
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
            val categoryId = config.categoryId

            combine(
                categoryRepository.getCategoryByIdFlow(categoryId!!),
                subcategoryRepository.getSubcategoriesByCategoryId(categoryId)
            ) { category, subcategories ->

                val safeCategory = category ?: error("Category not found")

                safeCategory.copy(
                    subcategories = subcategories
                )
            }
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
                color = form.color.resolveToHex(base.category.color),
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
            is CategoryEditorIntent.Action.DiscardDialogAction -> handleDiscardDialog(intent.action)

            is CategoryEditorIntent.Action.DeleteDialogAction -> handleDeleteDialog(intent.action)

            is CategoryEditorIntent.Action.IconSelectorDialogAction -> handleIconSelectorDialog(
                intent.action
            )

            is CategoryEditorIntent.Action.ColorSelectorDialogAction -> handleColorSelectorDialog(
                intent.action
            )

            CategoryEditorIntent.Action.SaveClicked -> handleSave()
            CategoryEditorIntent.Action.CloseClicked -> handleClose()
            CategoryEditorIntent.Action.AddSubcategoryClicked -> handleAddSubcategory()
            CategoryEditorIntent.Action.ShowInfoDialog -> handleInfoDialog(true)
            CategoryEditorIntent.Action.HideInfoDialog -> handleInfoDialog(false)
        }
    }

    private fun handleSave() {
        val state = uiState.value

        if (!state.isValid) {
            emitEvent(
                CategoryEditorEvent.ShowSnackbar(
                    messageRes = R.string.snackBar_delete_category_error,
                    actionLabelRes = null,
                    icon = IconPack.WARNING
                )
            )
            return
        }

        viewModelScope.launch {
            saveCategory(
                category = state.category,
                isEditMode = config.isEditMode
            )
            navigateBack()
        }
    }

    private fun handleClose() {
        if (uiState.value.hasUnsavedChanges) {
            handleDiscardDialog(ConfirmationDialogAction.Open)
        } else {
            navigateBack()
        }
    }

    private fun handleAddSubcategory() {
        navigateTo(Route.SubcategoryEditor.route)
    }

    private fun deleteCategory() {
        val category = uiState.value.category

        viewModelScope.launch {
            uiInternalState.update { it.copy(isDeleting = true) }

            runCatching {
                deleteCategoryWithReassign(category)
            }.onSuccess {
                uiInternalState.update { it.copy(isDeleting = false) }
                navigateBack()
            }.onFailure {
                uiInternalState.update { it.copy(isDeleting = false) }
                emitEvent(
                    CategoryEditorEvent.ShowSnackbar(
                        messageRes = R.string.snackBar_delete_category_error,
                        actionLabelRes = null,
                        icon = IconPack.WARNING
                    )
                )
            }
        }
    }

    private fun handleDeleteDialog(action: ConfirmationDialogAction) {
        handleConfirmationDialog(
            current = uiInternalState.value.deleteDialog,
            action = action,
            updateState = { newDialogState ->
                uiInternalState.update { currentState ->
                    currentState.copy(deleteDialog = newDialogState)
                }
            },
            onConfirmed = { deleteCategory() }
        )
    }

    private fun handleDiscardDialog(action: ConfirmationDialogAction) {
        handleConfirmationDialog(
            current = uiInternalState.value.discardDialog,
            action = action,
            updateState = { newDialogState ->
                uiInternalState.update { currentState ->
                    currentState.copy(discardDialog = newDialogState)
                }
            },
            onConfirmed = { navigateBack() }
        )
    }

    private fun handleIconSelectorDialog(action: SelectorDialogAction<IconPack>) {
        handleSelectorDialog(
            current = uiInternalState.value.iconSelectorDialog,
            action = action,
            updateState = { newDialogState ->
                uiInternalState.update { currentState ->
                    currentState.copy(iconSelectorDialog = newDialogState)
                }
            },
            onApplied = { onIntent(CategoryEditorIntent.State.IconSelected(it)) }
        )
    }

    private fun handleColorSelectorDialog(action: SelectorDialogAction<Color>) {
        handleSelectorDialog(
            current = uiInternalState.value.colorSelectorDialog,
            action = action,
            updateState = { newDialogState ->
                uiInternalState.update { currentState ->
                    currentState.copy(colorSelectorDialog = newDialogState)
                }
            },
            onApplied = { onIntent(CategoryEditorIntent.State.ColorSelected(it)) }
        )
    }

    private fun handleConfirmationDialog(
        current: ConfirmationDialogState,
        action: ConfirmationDialogAction,
        updateState: (ConfirmationDialogState) -> Unit,
        onConfirmed: () -> Unit
    ) {
        val (newState, effect) = reduceConfirmationDialog(current, action)
        updateState(newState)

        if (effect is ConfirmationDialogEffect.Confirmed) {
            onConfirmed()
        }
    }

    private fun handleInfoDialog(visible: Boolean) {
        uiInternalState.update { it.copy(infoDialog = InfoDialogState(visible)) }
    }

    private fun <T> handleSelectorDialog(
        current: SelectorDialogState<T>,
        action: SelectorDialogAction<T>,
        updateState: (SelectorDialogState<T>) -> Unit,
        onApplied: (T) -> Unit
    ) {
        val (newState, effect) = reduceSelectorDialog(current, action)
        updateState(newState)

        (effect as? SelectorDialogEffect.Applied)?.let {
            onApplied(it.value)
        }
    }

    private fun navigateBack() {
        emitEvent(CategoryEditorEvent.NavigateBack)
    }

    private fun navigateTo(route: String) {
        emitEvent(CategoryEditorEvent.NavigateTo(route))
    }

    private fun emitEvent(event: CategoryEditorEvent) {
        viewModelScope.launch { _events.emit(event) }
    }
}