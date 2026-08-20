package com.jorgelobo.koobe.ui.screen.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.app.AppViewModel
import com.jorgelobo.koobe.ui.components.composed.appBar.LogoAppBar
import com.jorgelobo.koobe.ui.navigation.handleBottomNavigation
import com.jorgelobo.koobe.ui.navigation.rememberBottomNavState
import com.jorgelobo.koobe.ui.screen.dashboard.components.DashboardBottomSection
import com.jorgelobo.koobe.ui.theme.AppTheme

/**
 * Displays the Dashboard screen.
 *
 * Observes the Dashboard UI state and application-level scheduled shortcut execution state,
 * displaying a Snackbar when recurring shortcuts have been automatically executed during
 * application startup.
 *
 * @param navController Controller used for application navigation.
 * @param appViewModel Application-level ViewModel providing global state and startup results.
 * @param viewModel ViewModel responsible for Dashboard-specific state and actions.
 */
@Composable
fun DashboardScreen(
    navController: NavController,
    appViewModel: AppViewModel,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentRoute = rememberBottomNavState(navController)
    val executedShortcuts by appViewModel.scheduledShortcutsExecuted.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    DashboardEffects(
        events = viewModel.events,
        navController = navController,
        executedShortcuts = executedShortcuts,
        snackbarHostState = snackbarHostState,
        onScheduledShortcutsFeedbackConsumed = appViewModel::clearScheduledShortcutsExecuted
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
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