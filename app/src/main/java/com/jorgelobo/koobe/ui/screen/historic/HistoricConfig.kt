package com.jorgelobo.koobe.ui.screen.historic

import kotlinx.serialization.Serializable

/**
 * Configuration data class for the Historic screen, defining its current state and interaction handlers.
 *
 * @property currentRoute The identifier of the currently active navigation route.
 * @property onRouteSelected Callback invoked when a specific route is selected from the history.
 */
@Serializable
data class HistoricConfig(
    val currentRoute: String,
    val onRouteSelected: (String) -> Unit
)