package com.jorgelobo.koobe.ui.screen.settings

/**
 * Sealed class representing UI events or user actions that can occur on the Settings screen.
 * These events are typically dispatched from the UI to the ViewModel to handle navigation
 * or state changes.
 */
sealed class SettingsEvent {
    object NavigateBack : SettingsEvent()
    data class NavigateTo(val route: String) : SettingsEvent()
}