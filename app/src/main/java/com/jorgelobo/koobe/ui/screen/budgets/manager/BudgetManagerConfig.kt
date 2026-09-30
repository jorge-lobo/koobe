package com.jorgelobo.koobe.ui.screen.budgets.manager

import kotlinx.serialization.Serializable

/**
 * Configuration data class for the Budget Manager screen, defining its current state and interaction handlers.
 *
 * @property currentRoute The identifier of the currently active navigation route.
 * @property onRouteSelected Callback invoked when a specific route is selected.
 */
@Serializable
data class BudgetManagerConfig(
    val currentRoute: String,
    val onRouteSelected: (String) -> Unit
)