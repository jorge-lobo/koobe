package com.jorgelobo.koobe.ui.navigation

import androidx.navigation.NavController

fun NavController.navigateClearingCurrent(route: String) {
    val currentDestinationId = currentBackStackEntry?.destination?.id

    navigate(route) {
        if (currentDestinationId != null) {
            popUpTo(currentDestinationId) {
                inclusive = true
            }
        }
    }
}