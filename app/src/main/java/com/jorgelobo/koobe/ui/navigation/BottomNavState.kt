package com.jorgelobo.koobe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun rememberBottomNavState(navController: NavController): String {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return remember(navBackStackEntry) {
        navBackStackEntry
            ?.destination
            ?.route
            ?.substringBefore("/")
            ?: ""
    }
}