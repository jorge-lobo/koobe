package com.jorgelobo.koobe.ui.screen.subcategories

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SubcategoryEditorViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SubcategoryEditorUiState.initialEmpty())
    val uiState: StateFlow<SubcategoryEditorUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SubcategoryEditorEvent>()
    val events = _events.asSharedFlow()

}