package com.jorgelobo.koobe.ui.screen.dashboard

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.navigation.handleBottomNavigation
import com.jorgelobo.koobe.ui.navigation.rememberBottomNavState

@Composable
fun DashboardScreen(
    navController: NavController
) {
    val currentRoute = rememberBottomNavState(navController)

    DashboardScreenUI(
        state = DashboardUiState(),
        currentRoute = currentRoute,
        onRouteSelected = { route ->
            navController.handleBottomNavigation(route)
        },
        onAddIncomeClick = { navController.navigate(Route.CategorySelector.create(TransactionType.INCOME.name)) },
        onAddExpenseClick = { navController.navigate(Route.CategorySelector.create(TransactionType.EXPENSE.name)) },
        onBudgetItemClick = {},
        onBudgetActionClick = {},
        onShortcutItemClick = {},
        onShortcutActionClick = {}
    )
}