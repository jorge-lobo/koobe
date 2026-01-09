package com.jorgelobo.koobe.ui.screen.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionEditorViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val subcategoryRepository: SubcategoryRepository,
    private val shortcutRepository: ShortcutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionEditorUiState.initialEmpty())
    val uiState: StateFlow<TransactionEditorUiState> = _uiState

    private lateinit var config: TransactionEditorConfig

    fun init(config: TransactionEditorConfig) {
        if (this::config.isInitialized) return
        this.config = config

        viewModelScope.launch {
            val category = categoryRepository.getCategoryById(config.categoryId) ?: Category.empty()
            val subcategory = config.subcategoryId?.let { subcategoryRepository.getSubcategoryById(it) }
            val shortcut = config.shortcutId?.let { shortcutRepository.getShortcutById(it) }

            _uiState.value = TransactionEditorUiState.initial(
                config = config,
                category = category,
                subcategory = subcategory,
                shortcut = shortcut
            )
        }
    }

}