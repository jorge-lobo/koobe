package com.jorgelobo.koobe.ui.screen.common

/**
 * Represents events that are sent from the ViewModel to the UI.
 * These are typically one-off events that trigger navigation or show transient UI elements.
 */
sealed class UiEvent {
    object NavigateBack : UiEvent()
}