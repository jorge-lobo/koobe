package com.jorgelobo.koobe.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.settings.theme.GetThemeOptionUseCase
import com.jorgelobo.koobe.domain.settings.theme.SetThemeOptionUseCase
import com.jorgelobo.koobe.domain.usecase.app.AppStartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appStartUseCase: AppStartUseCase,
    private val getThemeOptionUseCase: GetThemeOptionUseCase,
    private val setThemeOptionUseCase: SetThemeOptionUseCase
) : ViewModel() {

    private val _isInitializing = MutableStateFlow(true)
    val isInitializing = _isInitializing.asStateFlow()

    private val _themeOption = MutableStateFlow(ThemeOption.SYSTEM)
    val themeOption = _themeOption.asStateFlow()

    init {
        viewModelScope.launch {
            appStartUseCase()
            _isInitializing.value = false
        }

        viewModelScope.launch {
            getThemeOptionUseCase().collect {
                _themeOption.value = it
            }
        }
    }

    fun setTheme(option: ThemeOption) {
        viewModelScope.launch {
            setThemeOptionUseCase(option)
        }
    }
}