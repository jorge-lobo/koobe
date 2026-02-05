package com.jorgelobo.koobe.ui.screen.settings

data class SettingsConfig(
    val currentRoute: String,
    val onRouteSelected: (String) -> Unit,
)