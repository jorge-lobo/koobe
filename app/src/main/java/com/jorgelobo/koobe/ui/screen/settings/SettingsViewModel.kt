package com.jorgelobo.koobe.ui.screen.settings

import androidx.lifecycle.ViewModel
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    private lateinit var config: SettingsConfig

    fun init(config: SettingsConfig) {
        if (this::config.isInitialized) return
        this.config = config
    }

    // User actions

    /*fun onThemeOptionChange(themeOption: ThemeOption) {
        _uiState.update {
            it.copy(themeSelected = themeOption)
        }
    }*/
}