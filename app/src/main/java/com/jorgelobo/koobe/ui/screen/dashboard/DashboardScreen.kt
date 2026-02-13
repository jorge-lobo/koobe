package com.jorgelobo.koobe.ui.screen.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.composed.appBar.LogoAppBar
import com.jorgelobo.koobe.ui.navigation.handleBottomNavigation
import com.jorgelobo.koobe.ui.navigation.rememberBottomNavState
import com.jorgelobo.koobe.ui.screen.dashboard.components.DashboardBottomSection
import com.jorgelobo.koobe.ui.theme.AppTheme

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentRoute = rememberBottomNavState(navController)

    DashboardEffects(
        events = viewModel.events,
        navController = navController
    )

    Scaffold(
        topBar = { LogoAppBar() },
        bottomBar = {
            DashboardBottomSection(
                currentRoute = currentRoute,
                onRouteSelected = { route ->
                    navController.handleBottomNavigation(route)
                },
                onAddIncomeClick = { viewModel.onAddTransactionClick(TransactionType.INCOME) },
                onAddExpenseClick = { viewModel.onAddTransactionClick(TransactionType.EXPENSE) },
            )
        },
        containerColor = AppTheme.colors.backgroundColors.screenBackground
    ) { innerPadding ->
        DashboardScreenUI(
            modifier = Modifier.padding(innerPadding),
            state = uiState,
            onBudgetItemClick = { viewModel.onBudgetItemClick(it) },
            onBudgetActionClick = { viewModel.onBudgetActionClick(uiState.budgetItems.isNotEmpty()) },
            onShortcutItemClick = { viewModel.onShortcutItemClick(it) },
            onShortcutActionClick = { viewModel.onShortcutActionClick(uiState.shortcutItems.isNotEmpty()) }
        )
    }
}