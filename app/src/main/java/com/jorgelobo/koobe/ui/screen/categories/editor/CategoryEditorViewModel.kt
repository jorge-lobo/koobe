package com.jorgelobo.koobe.ui.screen.categories.editor

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class CategoryEditorViewModel @Inject constructor() : ViewModel() {

    private val _events = MutableSharedFlow<CategoryEditorEvent>()
    val events = _events.asSharedFlow()

}