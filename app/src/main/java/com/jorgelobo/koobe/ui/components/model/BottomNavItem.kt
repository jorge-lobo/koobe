package com.jorgelobo.koobe.ui.components.model

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: String,
    val isActive: Boolean = false
)