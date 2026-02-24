package com.jorgelobo.koobe.ui.screen.historic

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HistoricViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HistoricUiState())
    val uiState: StateFlow<HistoricUiState> = _uiState
    private val _events = MutableSharedFlow<HistoricEvent>()
    val events = _events.asSharedFlow()

    fun onBackClick() {
        navigateBack()
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