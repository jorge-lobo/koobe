package com.jorgelobo.koobe.ui.components.composed.navigation

data class BottomNavigationItem(
    val route: String,
    val label: String,
    val icon: String,
    val isActive: Boolean = false
)