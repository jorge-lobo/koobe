package com.jorgelobo.koobe.ui.screen.subcategories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.core.model.resolve
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.domain.usecase.subcategory.DeleteSubcategoryWithReassignUseCase
import com.jorgelobo.koobe.domain.usecase.subcategory.SaveSubcategoryCaseUse
import com.jorgelobo.koobe.domain.validation.NameValidationException
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.mappers.toSnackBarMessageRes
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.reduceConfirmationDialog
import com.jorgelobo.koobe.ui.screen.common.dialog.info.InfoDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState
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
 * ViewModel for the Subcategory Editor screen, supporting both creation and edit modes.
 *
 * The operation mode is determined by [SubcategoryEditorConfig], deserialized frm the `config`
 * navigation argument in [SavedStateHandle]. A missing config is a programing error and throws
 * immediately.
 *
 * Screen state is composed from persisted subcategory data, unsaved form edita, and transient
 * UI state.
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

    private var initialSnapshot: SubcategoryInitialSnapshot? = null

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

    private val baseStateFlow: Flow<SubcategoryEditorUiState> =
        combine(
            subcategoryFlow,
            categoryFlow
        ) { subcategory, category ->

            val safeCategory = category ?: Category.empty()
            val safeSubcategory = subcategory ?: Subcategory(
                id = 0,
                categoryId = safeCategory.id,
                name = "",
                icon = IconPack.PLACEHOLDER
            )

            if (initialSnapshot == null) {
                initialSnapshot = SubcategoryInitialSnapshot(
                    name = safeSubcategory.name,
                    icon = safeSubcategory.icon,
                    categoryId = safeSubcategory.categoryId
                )
            }

            SubcategoryEditorUiState(
                config = config,
                category = safeCategory,
                subcategory = safeSubcategory,
                nameInputState = InputState.DEFAULT,
                initialSnapshot = initialSnapshot!!
            )
        }

    /**
     * Reactive UI state observed by the screen.
     *
     * Composed from persisted subcategory data, unsaved form edits, and transient UI state.
     * The UI must recompose directly from this flow and never hold local copies.
     */
    val uiState: StateFlow<SubcategoryEditorUiState> =
        combine(
            baseStateFlow,
            formState,
            uiInternalState
        ) { base, form, uiInternal ->

            val nameInputState =
                if (uiInternal.hasTriedToSave && uiInternal.nameError != null) {
                    InputState.ERROR
                } else {
                    InputState.DEFAULT
                }

            val updatedSubcategory = base.subcategory.copy(
                name = form.name.resolve(base.subcategory.name),
                icon = form.icon.resolve(base.subcategory.icon),
                categoryId = form.categoryId.resolve(base.subcategory.categoryId)
            )

            base.copy(
                subcategory = updatedSubcategory,
                discardDialog = uiInternal.discardDialog,
                deleteDialog = uiInternal.deleteDialog,
                iconDialog = uiInternal.iconSelectorDialog,
                infoDialog = uiInternal.infoDialog,
                isSaving = uiInternal.isSaving,
                isDeleting = uiInternal.isDeleting,
                nameInputState = nameInputState,
                nameError = uiInternal.nameError
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = SubcategoryEditorUiState.initialEmpty()
            )

    /**
     * Single entry point for all UI interactions.
     *
     * - [SubcategoryEditorIntent.State] → handled synchronously by [SubcategoryEditorReducer].
     * - [SubcategoryEditorIntent.Action] → may trigger coroutines or navigation side effects.
     */
    fun onIntent(intent: SubcategoryEditorIntent) {
        when (intent) {

            is SubcategoryEditorIntent.Action -> handleAction(intent)

            is SubcategoryEditorIntent.State -> {

                if (intent is SubcategoryEditorIntent.State.NameChanged) {
                    uiInternalState.update {
                        it.copy(
                            nameError = null,
                            hasTriedToSave = false
                        )
                    }
                }

                val result = SubcategoryEditorReducer.reduce(
                    intent = intent,
                    currentForm = formState.value,
                    currentInternal = uiInternalState.value,
                    baseSubcategory = uiState.value.subcategory
                )

                formState.value = result.form
                uiInternalState.value = result.internal
            }
        }
    }

    /**
     * Handles action-based intents that trigger side effects, navigation, dialog interactions,
     * or persistence operations.
     */
    private fun handleAction(intent: SubcategoryEditorIntent.Action) {
        when (intent) {
            is SubcategoryEditorIntent.Action.DiscardDialogAction ->
                handleDiscardDialog(intent.action)

            is SubcategoryEditorIntent.Action.DeleteDialogAction ->
                handleDeleteDialog(intent.action)

            is SubcategoryEditorIntent.Action.IconSelectorDialogAction ->
                handleIconSelectorDialog(intent.action)

            SubcategoryEditorIntent.Action.SaveClicked -> handleSave()
            SubcategoryEditorIntent.Action.CloseClicked -> handleClose()
            SubcategoryEditorIntent.Action.ShowInfoDialog -> handleInfoDialog(true)
            SubcategoryEditorIntent.Action.HideInfoDialog -> handleInfoDialog(false)
            SubcategoryEditorIntent.Action.RequestDeleteSubcategory ->
                handleDeleteDialog(ConfirmationDialogAction.Open)
        }
    }

    /**
     * Persists the current subcategory after validating the editor state.
     *
     * Validation failures are reflected in the form state to allow field-level error presentation.
     */
    private fun handleSave() {
        val state = uiState.value

        if (!state.isValid) {
            emitEvent(
                SubcategoryEditorEvent.ShowSnackBar(
                    messageRes = R.string.snackBar_save_subcategory_error,
                    actionLabelRes = null,
                    icon = IconPack.WARNING
                )
            )
            return
        }

        viewModelScope.launch {
            uiInternalState.update { it.copy(isSaving = true) }

            runCatching {
                saveSubcategory(
                    subcategory = state.subcategory,
                    isEditMode = config.isEditMode
                )
            }.onSuccess {
                uiInternalState.update { it.copy(isSaving = false) }
                navigateBack()
            }.onFailure { error ->

                val validationError = error as? NameValidationException
                onIntent(SubcategoryEditorIntent.State.NameChanged(""))

                uiInternalState.update {
                    it.copy(
                        isSaving = false,
                        nameError = validationError,
                        hasTriedToSave = true
                    )
                }

                val messageRes = validationError?.toSnackBarMessageRes()
                    ?: R.string.snackBar_save_subcategory_error

                showSnackBar(messageRes)
            }
        }
    }

    /**
     * Requests screen closure.
     *
     * Displays a discard confirmation dialog when unsaved changes exist.
     */
    private fun handleClose() {
        if (formState.value.hasChanges) {
            handleDiscardDialog(ConfirmationDialogAction.Open)
        } else {
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
                uiInternalState.update { it.copy(isDeleting = false) }
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

    private fun handleDeleteDialog(action: ConfirmationDialogAction) {
        handleConfirmationDialog(
            current = uiInternalState.value.deleteDialog,
            action = action,
            updateState = { newDialogState ->
                uiInternalState.update { currentState ->
                    currentState.copy(deleteDialog = newDialogState)
                }
            },
            onConfirmed = { deleteSubcategory() }
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
            onApplied = { onIntent(SubcategoryEditorIntent.State.IconSelected(it)) }
        )
    }

    private fun handleInfoDialog(visible: Boolean) {
        uiInternalState.update { it.copy(infoDialog = InfoDialogState(visible)) }
    }

    /**
     * Centralizes confirmation dialog state transitions and confirmation effects.
     */
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

    /**
     * Centralizes selector dialog state transitions and applied selections.
     */
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
        emitEvent(SubcategoryEditorEvent.NavigateBack)
    }

    private fun showSnackBar(messageRes: Int) {
        emitEvent(
            SubcategoryEditorEvent.ShowSnackBar(
                messageRes,
                null,
                IconPack.WARNING
            )
        )
    }

    private fun emitEvent(event: SubcategoryEditorEvent) {
        viewModelScope.launch { _events.emit(event) }
    }
}