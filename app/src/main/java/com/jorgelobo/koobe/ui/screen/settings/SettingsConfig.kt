package com.jorgelobo.koobe.ui.screen.settings

/**
 * Configuration used when displaying the settings screen.
 *
 * @param currentRoute The currently selected route.
 * @param onRouteSelected Callback invoked when a route is selected.
 */
data class SettingsConfig(
    val currentRoute: String,
    val onRouteSelected: (String) -> Unit,
)