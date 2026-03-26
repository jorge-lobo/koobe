package com.jorgelobo.koobe.ui.screen.categories.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.usecase.category.GetCategoriesWithSubcategoriesUseCase
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogEffect
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.reduceConfirmationDialog
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryManagerViewModel @Inject constructor(
    private val getCategoriesWithSubcategories: GetCategoriesWithSubcategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryManagerUiState())
    val uiState: StateFlow<CategoryManagerUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CategoryManagerEvent>()
    val events = _events.asSharedFlow()

    init {
        collectCategories()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectCategories() {
        viewModelScope.launch {
            _uiState
                .map { it.transactionTypeSelected }
                .distinctUntilChanged()
                .flatMapLatest { type ->
                    getCategoriesWithSubcategories(type)
                }
                .collect { categories ->
                    updateState {
                        copy(
                            categories = categories,
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
                expandedCategoryId = null,
                isLoading = true
            )
        }
    }

    fun onCategoryExpandToggle(categoryId: Int) {
        updateState {
            copy(
                expandedCategoryId = if (expandedCategoryId == categoryId) null else categoryId
            )
        }
    }

    fun onBackClick() {
        navigateBack()
    }

    fun onAddCategoryClick() {
        val route = Route.CategoryEditor.create(
            CategoryEditorConfig(
                categoryId = null
            )
        )
        navigateTo(route)
    }

    fun onEditCategory(categoryId: Int) {
        val route = Route.CategoryEditor.create(
            CategoryEditorConfig(
                categoryId = categoryId
            )
        )
        navigateTo(route)
    }

    fun onAddSubcategoryClick(categoryId: Int) {
        val route = Route.SubcategoryEditor.create(
            SubcategoryEditorConfig(
                subcategoryId = null,
                categoryId = categoryId
            )
        )
        navigateTo(route)
    }

    fun onEditSubcategory(categoryId: Int, subcategoryId: Int) {
        val route = Route.SubcategoryEditor.create(
            SubcategoryEditorConfig(
                categoryId = categoryId,
                subcategoryId = subcategoryId
            )
        )
        navigateTo(route)
    }

    fun onDeleteSubcategoryClick(subcategoryId: Int) {
        updateState {
            copy(
                deleteDialog = deleteDialog.copy(
                    visible = true,
                    targetId = subcategoryId
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
            copy(
                deleteDialog = dialogState
            )
        }

        when (effect) {
            ConfirmationDialogEffect.Confirmed -> deleteSubcategory()

            null -> Unit
        }
    }

    private fun deleteSubcategory() {
        val id = uiState.value.deleteDialog.targetId ?: return

        // TODO: Delete subcategory
    }

    private fun navigateTo(route: String) {
        emitEvent(CategoryManagerEvent.NavigateTo(route))
    }

    private fun navigateBack() {
        emitEvent(CategoryManagerEvent.NavigateBack)
    }

    private fun emitEvent(event: CategoryManagerEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    private fun updateState(reducer: CategoryManagerUiState.() -> CategoryManagerUiState) {
        _uiState.update { it.reducer() }
    }
}