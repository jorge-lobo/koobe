package com.jorgelobo.koobe.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.settings.GetUserSettingsUseCase
import com.jorgelobo.koobe.domain.settings.SetThemeOptionUseCase
import com.jorgelobo.koobe.domain.usecase.app.AppStartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the global application state, including initialization
 * processes and user preference settings such as theme selection.
 *
 * @property appStartUseCase Use case to handle initial application logic upon startup.
 * @property getUserSettingsUseCase Use case to retrieve the current user settings flow.
 * @property setThemeOptionUseCase Use case to update the application's theme preference.
 */
@HiltViewModel
class AppViewModel @Inject constructor(
    private val appStartUseCase: AppStartUseCase,
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
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
            getUserSettingsUseCase()
                .map { it.theme }
                .collect { _themeOption.value = it }
        }
    }

    /**
     * Updates the application's theme setting.
     *
     * This function launches a coroutine in the [viewModelScope] to persist the selected
     * [ThemeOption] using the [setThemeOptionUseCase].
     *
     * @param option The [ThemeOption] to be applied and saved.
     */
    fun setTheme(option: ThemeOption) {
        viewModelScope.launch {
            setThemeOptionUseCase(option)
        }
    }
}