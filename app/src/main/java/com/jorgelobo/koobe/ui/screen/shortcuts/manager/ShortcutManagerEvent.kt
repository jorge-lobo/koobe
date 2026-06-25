package com.jorgelobo.koobe.ui.screen.shortcuts.manager

sealed class ShortcutManagerEvent {
    data object NavigateBack : ShortcutManagerEvent()
    data class NavigateTo(val route: String) : ShortcutManagerEvent()
}