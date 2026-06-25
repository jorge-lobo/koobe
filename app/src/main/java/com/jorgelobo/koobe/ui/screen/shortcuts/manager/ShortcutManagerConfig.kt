package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import kotlinx.serialization.Serializable

@Serializable
data class ShortcutManagerConfig(
    val currentRoute: String,
    val onRouteSelected: (String) -> Unit
)