package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.usecase.category.GetAllCategoriesUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.DeleteShortcutUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.GetAllShortcutsByTypeUseCase
import com.jorgelobo.koobe.domain.usecase.shortcut.GetShortcutByIdUseCase
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorTarget
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.reduceConfirmationDialog
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShortcutManagerViewModel @Inject constructor(
    private val getAllShortcuts: GetAllShortcutsByTypeUseCase,
    private val getAllCategories: GetAllCategoriesUseCase,
    private val getShortcutById: GetShortcutByIdUseCase,
    private val deleteShortcut: DeleteShortcutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ShortcutManagerUiState(
            transactionTypeSelected = TransactionType.EXPENSE,
            isLoading = true
        )
    )
    val uiState: StateFlow<ShortcutManagerUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ShortcutManagerEvent>()
    val events = _events.asSharedFlow()

    init {
        collectShortcuts()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectShortcuts() {
        viewModelScope.launch {
            _uiState
                .map { it.transactionTypeSelected to it.sortingSelector.selected }
                .distinctUntilChanged()
                .flatMapLatest { (type, sorting) ->

                    updateState {
                        copy(
                            transactionTypeSelected = type,
                            isLoading = true,
                            errorMessage = null
                        )
                    }

                    combine(
                        getAllShortcuts(type),
                        getAllCategories()
                    ) { shortcuts, categories ->

                        val categoriesById = categories.associateBy(Category::id)

                        shortcuts.mapNotNull { shortcut ->

                            val category = categoriesById[shortcut.categoryId]
                                ?: return@mapNotNull null

                            ShortcutItemUi(shortcut, category)
                        }
                            .sortedBy(sorting)
                    }
                        .catch { error ->

                            updateState {
                                copy(
                                    isLoading = false,
                                    errorMessage = error.message
                                )
                            }

                            emit(emptyList())
                        }
                }
                .collect { shortcutItems ->

                    updateState {
                        copy(
                            shortcutItems = shortcutItems,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onTransactionTypeChange(type: TransactionType) {
        updateState {
            copy(
                transactionTypeSelected = type,
                isLoading = true
            )
        }
    }

    fun onBackClick() {
        navigateBack()
    }

    fun onAddShortcutClick() {
        val route = Route.CategorySelector.create(
            CategorySelectorConfig(
                mode = CategorySelectorMode.CREATE_SHORTCUT,
                target = CategorySelectorTarget.SHORTCUT_EDITOR,
                initialTransactionType = uiState.value.transactionTypeSelected
            )
        )
        navigateTo(route)
    }

    fun onEditShortcut(shortcutId: Int) {
        val route = Route.ShortcutEditor.create(
            ShortcutEditorConfig.Edit(shortcutId)
        )
        navigateTo(route)
    }

    fun onDeleteShortcutClick(shortcutId: Int) {
        updateState {
            copy(
                deleteDialog = deleteDialog.copy(
                    visible = true,
                    targetId = shortcutId
                )
            )
        }
    }

    fun onDeleteDialogAction(action: ConfirmationDialogAction) {
        val (dialogState, effect) = reduceConfirmationDialog(
            state = uiState.value.deleteDialog,
            action = action
        )

        updateState {
            copy(deleteDialog = dialogState)
        }

        when (effect) {
            ConfirmationDialogEffect.Confirmed -> performDeleteShortcut()

            null -> Unit
        }
    }

    private fun performDeleteShortcut() {
        viewModelScope.launch {
            val id = uiState.value.deleteDialog.targetId ?: return@launch
            val shortcut = getShortcutById(id) ?: return@launch

            runCatching {
                deleteShortcut(shortcut)
            }.onFailure { error ->
                updateState {
                    copy(errorMessage = error.message)
                }
            }
        }
    }

    private fun navigateTo(route: String) {
        emitEvent(ShortcutManagerEvent.NavigateTo(route))
    }

    private fun navigateBack() {
        emitEvent(ShortcutManagerEvent.NavigateBack)
    }

    private fun emitEvent(event: ShortcutManagerEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    private fun updateState(reducer: ShortcutManagerUiState.() -> ShortcutManagerUiState) {
        _uiState.update { it.reducer() }
    }
}