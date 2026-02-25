package com.jorgelobo.koobe.ui.screen.historic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HistoricUiState())
    val uiState: StateFlow<HistoricUiState> = _uiState

    private val _events = MutableSharedFlow<HistoricEvent>()
    val events = _events.asSharedFlow()

    fun onTransactionTypeChanged(type: TransactionType) {
        if (_uiState.value.transactionTypeSelected == type) return

        _uiState.update { it.copy(transactionTypeSelected = type) }
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

    private fun navigateBack() {
        emitEvent(HistoricEvent.NavigateBack)
    }

    private fun navigateTo(route: String) {
        emitEvent(HistoricEvent.NavigateTo(route))
    }

    private fun emitEvent(event: HistoricEvent) {
        viewModelScope.launch { _events.emit(event) }
    }
}