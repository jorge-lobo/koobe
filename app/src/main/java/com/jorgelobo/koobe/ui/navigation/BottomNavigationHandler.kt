package com.jorgelobo.koobe.ui.navigation

import androidx.navigation.NavController

fun NavController.handleBottomNavigation(route: String) {
    val currentRoute = this.currentBackStackEntry?.destination?.route?.substringBefore("/")

    if (currentRoute == route) {
        return
    }

    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(this@handleBottomNavigation.graph.startDestinationId) {
            saveState = true
        }
    }
}