package com.jorgelobo.koobe.ui.screen.categories.selector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.constants.enums.CategoryDetailType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategorySelectorViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val subcategoryRepository: SubcategoryRepository,
    private val shortcutRepository: ShortcutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategorySelectorUiState.initialEmpty())
    val uiState: StateFlow<CategorySelectorUiState> = _uiState

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    private lateinit var config: CategorySelectorConfig

    fun init(config: CategorySelectorConfig) {
        if (this::config.isInitialized) return
        this.config = config

        _uiState.value = CategorySelectorUiState.initial(config)

        loadCategories()
    }

    // User actions

    fun onTransactionTypeChanged(type: TransactionType) {
        _uiState.update {
            it.copy(
                transactionType = type,
                selectedCategoryId = null,
                selectedSubcategoryId = null,
                selectedShortcutId = null,
                step = SelectorStep.SelectCategory
            )
        }

        loadCategories()
    }

    fun onChangeCategoryClick() {
        _uiState.update { it.copy(step = SelectorStep.SelectCategory) }
    }

    fun onCategorySelected(categoryId: Int) {
        _uiState.update {
            it.copy(
                selectedCategoryId = categoryId,
                selectedSubcategoryId = null,
                selectedShortcutId = null
            )
        }

        if (config.mode.requiresSubcategorySelection) {
            loadCategoryDetails(categoryId)
            _uiState.update { it.copy(step = SelectorStep.SelectSubcategory) }
        } else {
            updatePrimaryActionState()
        }
    }

    fun onSubcategorySelected(subcategoryId: Int) {
        _uiState.update { it.copy(selectedSubcategoryId = subcategoryId) }
        updatePrimaryActionState()
    }

    fun onShortcutSelected(shortcutId: Int) {
        _uiState.update { it.copy(selectedShortcutId = shortcutId) }
        updatePrimaryActionState()
    }

    fun onCategoryDetailSelected(type: CategoryDetailType) {
        _uiState.update {
            it.copy(
                categoryDetailSelected = type,
                selectedSubcategoryId = null,
                selectedShortcutId = null
            )
        }
        updatePrimaryActionState()
    }

    fun onBackRequested() {
        val state = _uiState.value

        if (state.hasUnsavedChanges) {
            _uiState.update { it.copy(showDiscardDialog = true) }
        } else {
            viewModelScope.launch {
                _events.emit(UiEvent.NavigateBack)
            }
        }
    }

    fun onDiscardDialogDismiss() {
        _uiState.update { it.copy(showDiscardDialog = false) }
    }

    fun onDiscardConfirmed() {
        _uiState.update { it.copy(showDiscardDialog = false) }

        viewModelScope.launch {
            _events.emit(UiEvent.NavigateBack)
        }
    }

    // Data loading

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val allCategories = categoryRepository.getAllCategories().first()

                val filteredCategories = allCategories
                    .filter { it.type == _uiState.value.transactionType }
                    .sortedBy { it.id }

                _uiState.update {
                    it.copy(
                        categories = filteredCategories,
                        isLoading = false
                    )
                }

                updatePrimaryActionState()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    private fun loadCategoryDetails(categoryId: Int) {
        viewModelScope.launch {
            val subcategories =
                subcategoryRepository.getSubcategoriesByCategoryId(categoryId).first()
            val shortcuts = shortcutRepository.getShortcutsByCategoryId(categoryId).first()

            _uiState.update {
                it.copy(
                    subcategories = subcategories,
                    shortcuts = shortcuts
                )
            }
        }
    }

    // State helpers

    private fun updatePrimaryActionState() {
        val state = _uiState.value

        val enabled = when (state.step) {
            SelectorStep.SelectCategory -> state.selectedCategoryId != null && (!config.mode.requiresSubcategorySelection)

            SelectorStep.SelectSubcategory -> when (state.categoryDetailSelected) {
                CategoryDetailType.SUBCATEGORIES -> state.selectedSubcategoryId != null
                CategoryDetailType.SHORTCUTS -> state.selectedShortcutId != null
            }
        }

        _uiState.update { it.copy(isPrimaryActionEnabled = enabled) }
    }
}

sealed class UiEvent {
    object NavigateBack : UiEvent()
}