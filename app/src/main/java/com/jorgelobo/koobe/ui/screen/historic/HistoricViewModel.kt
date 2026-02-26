package com.jorgelobo.koobe.ui.screen.historic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.category.CategoryHistory
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.domain.usecase.historic.GetHistoricDataUseCase
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricViewModel @Inject constructor(
    private val getHistoricData: GetHistoricDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoricUiState())
    val uiState: StateFlow<HistoricUiState> = _uiState

    private val _events = MutableSharedFlow<HistoricEvent>()
    val events = _events.asSharedFlow()

    private var loadJob: Job? = null

    init {
        loadHistoric()
    }

    fun onTransactionTypeChanged(type: TransactionType) {
        if (_uiState.value.transactionTypeSelected == type) return

        _uiState.update { it.copy(transactionTypeSelected = type) }

        loadHistoric()
    }

    fun onBackClick() {
        navigateBack()
    }

    fun onTransactionItemClick(item: Transaction) {
        val route = Route.TransactionEditor.create(
            TransactionEditorConfig(
                transactionId = item.id,
                transactionType = item.type,
                categoryId = item.categoryId,
                subcategoryId = item.subcategoryId,
                originRoute = "historic"
            )
        )
        navigateTo(route)
    }

    fun onCategoryExpandToggle(categoryId: Int) {
        _uiState.update { state ->
            val updated = state.categories.map { categoryUi ->

                if (categoryUi.category.id == categoryId) {
                    val newExpanded = !categoryUi.isExpanded

                    categoryUi.copy(isExpanded = newExpanded,
                        expandedSubcategories = if (newExpanded) {
                            categoryUi.expandedSubcategories
                        } else {
                            emptySet()
                        }
                    )
                } else {
                    categoryUi
                }
            }

            state.copy(categories = updated)
        }
    }

    fun onSubcategoryExpandToggle(
        categoryId: Int,
        subcategoryId: Int
    ) {
        _uiState.update { state ->
            val updated = state.categories.map { categoryUi ->

                if (categoryUi.category.id != categoryId) return@map categoryUi

                val newExpandedSet =
                    if (subcategoryId in categoryUi.expandedSubcategories) {
                        categoryUi.expandedSubcategories - subcategoryId
                    } else {
                        categoryUi.expandedSubcategories + subcategoryId
                    }

                categoryUi.copy(expandedSubcategories = newExpandedSet)
            }

            state.copy(categories = updated)
        }
    }

    fun loadHistoric() {
        _uiState.update { it.copy(isLoading = true) }

        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            getHistoricData(_uiState.value.transactionTypeSelected).collect { histories ->

                _uiState.update { state ->
                    state.copy(
                        categories = histories.map { it.toUi() },
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun navigateBack() {
        emitEvent(HistoricEvent.NavigateBack)
    }

    private fun navigateTo(route: String) {
        emitEvent(HistoricEvent.NavigateTo(route))
    }

    private fun emitEvent(event: HistoricEvent) {
        viewModelScope.launch { _events.emit(event) }
    }

    private fun CategoryHistory.toUi() =
        CategoryHistoricUi(
            category = category,
            history = this
        )
}