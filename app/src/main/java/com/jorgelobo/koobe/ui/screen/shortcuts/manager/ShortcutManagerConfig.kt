package com.jorgelobo.koobe.ui.screen.shortcuts.manager

/**
 * Configuration data class for the Shortcut Manager screen.
 *
 * @property currentRoute The currently active navigation route.
 * @property onRouteSelected Callback triggered when a new route is selected from the manager.
 */
data class ShortcutManagerConfig(
    val currentRoute: String,
    val onRouteSelected: (String) -> Unit
)