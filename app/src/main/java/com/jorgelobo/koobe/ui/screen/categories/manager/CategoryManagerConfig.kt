package com.jorgelobo.koobe.ui.screen.categories.manager

import kotlinx.serialization.Serializable

@Serializable
data class CategoryManagerConfig(
    val currentRoute: String,
    val onRouteSelected: (String) -> Unit
)