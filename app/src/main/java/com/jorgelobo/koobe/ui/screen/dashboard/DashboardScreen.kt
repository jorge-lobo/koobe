package com.jorgelobo.koobe.ui.screen.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun DashboardScreen(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    DashboardScreenUI(
        state = DashboardUiState(),
        currentRoute = currentRoute,
        onRouteSelected = { route -> navController.navigate(route) },
        onAddIncomeClick = {},
        onAddExpenseClick = {},
        onBudgetItemClick = {},
        onBudgetActionClick = {},
        onShortcutItemClick = {},
        onShortcutActionClick = {}
    )
}