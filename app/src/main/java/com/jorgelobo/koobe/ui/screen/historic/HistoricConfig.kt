package com.jorgelobo.koobe.ui.screen.historic

import kotlinx.serialization.Serializable

@Serializable
data class HistoricConfig(
    val currentRoute: String,
    val onRouteSelected: (String) -> Unit
)