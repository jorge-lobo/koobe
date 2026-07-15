package com.jorgelobo.koobe.ui.screen.shortcuts.manager

/**
 * Represents one-time UI events (side effects) emitted by the Shortcut Manager screen.
 */
sealed class ShortcutManagerEvent {
    data object NavigateBack : ShortcutManagerEvent()
    data class NavigateTo(val route: String) : ShortcutManagerEvent()
}