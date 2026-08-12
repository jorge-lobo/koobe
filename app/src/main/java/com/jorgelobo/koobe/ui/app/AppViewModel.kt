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
 * ViewModel responsible for managing global application state.
 *
 * Handles application startup tasks, user preference observation, and theme configuration.
 * It also exposes the number of scheduled shortcuts executed during application startup so that
 * the appropriate UI can provide feedback to the user.
 *
 * @property appStartUseCase Use case responsible for executing application startup tasks.
 * @property getUserSettingsUseCase Use case for observing the current user settings.
 * @property setThemeOptionUseCase Use case for updating the application's theme preference.
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

    /**
     * Number of scheduled shortcuts executed during the current application startup.
     *
     * A value of zero indicates that no scheduled shortcuts were executed.
     * The value is reset after the corresponding UI feedback has been consumed.
     */
    private val _scheduledShortcutsExecuted = MutableStateFlow(0)
    val scheduledShortcutsExecuted = _scheduledShortcutsExecuted.asStateFlow()

    init {
        viewModelScope.launch {
            val executedShortcuts = appStartUseCase()

            _scheduledShortcutsExecuted.value = executedShortcuts
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

    /**
     * Clears the scheduled shortcut execution count after it has been consumed by the UI.
     */
    fun clearScheduledShortcutsExecuted() {
        _scheduledShortcutsExecuted.value = 0
    }
}