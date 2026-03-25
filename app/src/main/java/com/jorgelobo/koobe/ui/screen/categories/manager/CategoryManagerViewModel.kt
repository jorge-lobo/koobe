package com.jorgelobo.koobe.ui.screen.categories.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.usecase.category.GetCategoriesWithSubcategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private fun updateState(reducer: CategoryManagerUiState.() -> CategoryManagerUiState) {
        _uiState.update { it.reducer() }
    }
}