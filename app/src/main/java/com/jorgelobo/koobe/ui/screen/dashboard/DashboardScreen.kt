package com.jorgelobo.koobe.ui.screen.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.navigation.handleBottomNavigation
import com.jorgelobo.koobe.ui.navigation.rememberBottomNavState
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorMode
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorTarget

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentRoute = rememberBottomNavState(navController)

    DashboardScreenUI(
        state = uiState,
        currentRoute = currentRoute,
        onRouteSelected = { route ->
            navController.handleBottomNavigation(route)
        },
        onAddIncomeClick = {
            navController.navigate(
                Route.CategorySelector.create(
                    CategorySelectorConfig(
                        mode = CategorySelectorMode.CREATE_TRANSACTION,
                        target = CategorySelectorTarget.TRANSACTION_EDITOR,
                        initialTransactionType = TransactionType.INCOME
                    )
                )
            )
        },
        onAddExpenseClick = {
            navController.navigate(
                Route.CategorySelector.create(
                    CategorySelectorConfig(
                        mode = CategorySelectorMode.CREATE_TRANSACTION,
                        target = CategorySelectorTarget.TRANSACTION_EDITOR,
                        initialTransactionType = TransactionType.EXPENSE
                    )
                )
            )
        },
        onBudgetItemClick = { navController.navigate(Route.BudgetEditor.create(it.budget.id)) },
        onBudgetActionClick = {
            if (uiState.budgetItems.isEmpty()) navController.navigate(Route.BudgetEditor.create(0))
            else navController.navigate(Route.BudgetManager.route)
        },
        onShortcutItemClick = { navController.navigate(Route.ShortcutEditor.create(it.shortcut.id)) },
        onShortcutActionClick = {
            if (uiState.shortcutItems.isEmpty()) navController.navigate(
                Route.ShortcutEditor.create(0)
            ) else navController.navigate(Route.ShortcutManager.route)
        }
    )
}