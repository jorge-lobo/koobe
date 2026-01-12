package com.jorgelobo.koobe.ui.screen.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.amount.reduceAmountInput
import com.jorgelobo.koobe.ui.mappers.toAmountAction
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.repository.CategoryRepository
import com.jorgelobo.koobe.domain.repository.ShortcutRepository
import com.jorgelobo.koobe.domain.repository.SubcategoryRepository
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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
            val subcategory =
                config.subcategoryId?.let { subcategoryRepository.getSubcategoryById(it) }
            val shortcut = config.shortcutId?.let { shortcutRepository.getShortcutById(it) }

            _uiState.value = TransactionEditorUiState.initial(
                config = config,
                category = category,
                subcategory = subcategory,
                shortcut = shortcut
            )
        }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun onResetDescription() {
        _uiState.update { state ->
            state.copy(description = "")
        }
    }


    fun onKeyClicked(key: KeypadKey) {
        val action = key.toAmountAction()

        _uiState.update { state ->
            val newInput = reduceAmountInput(
                current = state.amountInput,
                action = action
            )

            state.copy(
                amountInput = newInput,
                amount = newInput.toDoubleOrNull() ?: 0.0
            )
        }
    }

    fun onResetAmount() {
        _uiState.update { state ->
            state.copy(
                amountInput = "0",
                amount = 0.0
            )
        }
    }
}