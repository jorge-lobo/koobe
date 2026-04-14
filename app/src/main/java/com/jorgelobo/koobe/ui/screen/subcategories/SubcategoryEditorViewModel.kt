package com.jorgelobo.koobe.ui.screen.subcategories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.domain.usecase.subcategory.DeleteSubcategoryWithReassignUseCase
import com.jorgelobo.koobe.domain.usecase.subcategory.SaveSubcategoryCaseUse
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.reduceConfirmationDialog
import com.jorgelobo.koobe.ui.screen.common.dialog.info.InfoDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.reduceSelectorDialog
import com.jorgelobo.koobe.ui.screen.subcategories.state.SubcategoryFormState
import com.jorgelobo.koobe.ui.screen.subcategories.state.SubcategoryUiStateInternal
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
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import javax.inject.Inject

/**
 * ViewModel for the Subcategory Editor screen.
 *
 * Supports both creation and editing modes based on [SubcategoryEditorConfig].
 * Manages form state, validation, unsaved changes, and dialog interactions.
 *
 * Emits navigation and UI events via [events].
 */
@HiltViewModel
class SubcategoryEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    subcategoryRepository: SubcategoryRepository,
    private val categoryRepository: CategoryRepository,
    private val saveSubcategory: SaveSubcategoryCaseUse,
    private val deleteSubcategoryWithReassign: DeleteSubcategoryWithReassignUseCase
) : ViewModel() {

    private val _events = MutableSharedFlow<SubcategoryEditorEvent>()
    val events = _events.asSharedFlow()

    /**
     * Screen configuration (edit/create mode and initial IDs).
     */
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

    private val formState = MutableStateFlow(SubcategoryFormState())
    private val uiInternalState = MutableStateFlow(SubcategoryUiStateInternal())

    /**
     * Provides initial UI state from repository data.
     *
     * Handles:
     * - Edit mode: loads existing subcategory
     * - Create mode: initializes empty subcategory
     */
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
                    subcategory = Subcategory(
                        id = 0,
                        categoryId = safeCategory.id,
                        name = "",
                        icon = IconPack.PLACEHOLDER
                    )
                )
            }
        }

    /**
     * Combined UI state:
     * - Base data (repository)
     * - User input (form)
     * - Transient UI state (dialogs, loading)
     */
    val uiState: StateFlow<SubcategoryEditorUiState> =
        combine(
            baseStateFlow,
            formState,
            uiInternalState
        ) { base, form, uiInternal ->

            val updatedSubcategory = base.subcategory.copy(
                name = form.name ?: base.subcategory.name,
                icon = form.icon ?: base.subcategory.icon,
                categoryId = form.categoryId ?: base.subcategory.categoryId
            )

            val newState = base.copy(
                subcategory = updatedSubcategory,
                discardDialog = uiInternal.discardDialog ?: base.discardDialog,
                deleteDialog = uiInternal.deleteDialog ?: base.deleteDialog,
                iconDialog = uiInternal.iconSelectorDialog ?: base.iconDialog,
                infoDialog = uiInternal.infoDialog ?: base.infoDialog,
                isDeleting = uiInternal.isDeleting
            )

            newState.copy(
                isSaveButtonEnabled = computeSaveEnabled(newState)
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = SubcategoryEditorUiState.initialEmpty()
            )

    /**
     * Determines whether the save action should be enabled based on the current state of the form.
     *
     * The save button is enabled if:
     * 1. The current form data is valid (e.g., required fields are populated).
     * 2. In edit mode: there are actual changes compared to the original data.
     * 3. In creation mode: the data is valid.
     *
     * @param state The current UI state containing form values and validation logic.
     * @return `true` if the subcategory can be saved, `false` otherwise.
     */
    private fun computeSaveEnabled(state: SubcategoryEditorUiState): Boolean {
        val isValid = state.isValid

        return when {
            !isValid -> false
            config.isEditMode -> state.hasUnsavedChanges
            else -> true
        }
    }

    fun onNameChanged(name: String) {
        formState.update { it.copy(name = name) }
    }

    fun onResetName() {
        formState.update { it.copy(name = "") }
    }

    fun onIconSelected(icon: IconPack) {
        formState.update { it.copy(icon = icon) }
    }

    fun onCategoryChanged(id: Int) {
        formState.update { it.copy(categoryId = id) }
    }

    /**
     * Saves the subcategory and navigates back on success.
     */
    fun onSaveClick() {
        val state = uiState.value

        if (state.subcategory.categoryId <= 0) {
            return
        }

        viewModelScope.launch {
            saveSubcategory(
                subcategory = state.subcategory.copy(
                    name = state.subcategory.name,
                    icon = state.subcategory.icon,
                    categoryId = state.subcategory.categoryId
                ),
                isEditMode = config.isEditMode
            )
            navigateBack()
        }
    }

    /**
     * Deletes the subcategory and reassigns related data.
     *
     * Shows loading state and handles failure via snackbar.
     */
    private fun deleteSubcategory() {
        val subcategory = uiState.value.subcategory

        viewModelScope.launch {
            uiInternalState.update { it.copy(isDeleting = true) }

            runCatching {
                deleteSubcategoryWithReassign(subcategory)
            }.onSuccess {
                navigateBack()
            }.onFailure {
                uiInternalState.update { it.copy(isDeleting = false) }
                emitEvent(
                    SubcategoryEditorEvent.ShowSnackBar(
                        messageRes = R.string.snackBar_delete_subcategory_error,
                        actionLabelRes = null,
                        icon = IconPack.WARNING
                    )
                )
            }
        }
    }

    fun onCloseClick() {
        val state = uiState.value

        if (state.hasUnsavedChanges) {
            onDiscardDialogAction(ConfirmationDialogAction.Open)
        } else {
            navigateBack()
        }
    }

    fun onDiscardDialogAction(action: ConfirmationDialogAction) {
        val (dialogState, effect) = reduceConfirmationDialog(
            state = uiState.value.discardDialog,
            action = action
        )

        uiInternalState.update {
            it.copy(discardDialog = dialogState)
        }

        when (effect) {
            ConfirmationDialogEffect.Confirmed -> navigateBack()

            null -> Unit
        }
    }

    fun onDeleteDialogAction(action: ConfirmationDialogAction) {
        val (dialogState, effect) = reduceConfirmationDialog(
            state = uiState.value.deleteDialog,
            action = action
        )

        uiInternalState.update {
            it.copy(deleteDialog = dialogState)
        }

        when (effect) {
            ConfirmationDialogEffect.Confirmed -> deleteSubcategory()

            null -> Unit
        }
    }

    fun onInfoDialogOpen() {
        uiInternalState.update { it.copy(infoDialog = InfoDialogState(visible = true)) }
    }

    fun dismissInfoDialog() {
        uiInternalState.update { it.copy(infoDialog = InfoDialogState(visible = false)) }
    }

    fun onIconSelectorAction(action: SelectorDialogAction<IconPack>) {
        val currentState = uiState.value
        val currentDialogState = uiInternalState.value.iconSelectorDialog ?: currentState.iconDialog

        val baseState =
            when (action) {
                is SelectorDialogAction.Open -> {
                    currentDialogState.copy(
                        initial = currentState.subcategory.icon,
                        selected = currentState.subcategory.icon
                    )
                }

                is SelectorDialogAction.Select -> {
                    currentDialogState.copy(
                        selected = action.item
                    )
                }

                else -> currentDialogState
            }

        val (dialogState, effect) = reduceSelectorDialog(
            state = baseState,
            action = action
        )

        uiInternalState.update {
            it.copy(iconSelectorDialog = dialogState)
        }

        when (effect) {
            is SelectorDialogEffect.Applied -> formState.update { it.copy(icon = effect.value) }

            null -> Unit
        }
    }

    private fun navigateBack() {
        emitEvent(SubcategoryEditorEvent.NavigateBack)
    }

    private fun emitEvent(event: SubcategoryEditorEvent) {
        viewModelScope.launch { _events.emit(event) }
    }
}