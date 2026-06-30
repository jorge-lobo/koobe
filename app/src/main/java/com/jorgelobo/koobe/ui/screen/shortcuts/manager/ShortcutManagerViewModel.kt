package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.usecase.shortcut.GetAllShortcutsByTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShortcutManagerViewModel @Inject constructor(
    private val getAllShortcuts: GetAllShortcutsByTypeUseCase
) : ViewModel() {

    private val selectedType = MutableStateFlow(TransactionType.EXPENSE)

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
            selectedType
                .flatMapLatest { type ->
                    updateState {
                        copy(
                            transactionTypeSelected = type,
                            isLoading = true,
                            errorMessage = null
                        )
                    }
                    getAllShortcuts(type)
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
                .collect { shortcuts ->
                    updateState {
                        copy(
                            shortcutItems = shortcuts,
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