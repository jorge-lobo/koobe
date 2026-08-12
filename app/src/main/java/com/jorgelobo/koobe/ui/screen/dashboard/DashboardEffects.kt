package com.jorgelobo.koobe.ui.screen.dashboard

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.pluralStringResource
import androidx.navigation.NavController
import com.jorgelobo.koobe.ui.app.AppViewModel
import com.jorgelobo.koobe.R
import kotlinx.coroutines.flow.Flow

/**
 * Side-effect handler for the Dashboard screen.
 *
 * This composable listens to the [events] flow and performs UI-related actions
 * that are not part of the state, such as navigation.
 *
 * @param events A [Flow] of [DashboardEvent]s to be handled.
 * @param navController The [NavController] used to perform navigation actions.
 */
@Composable
fun DashboardEffects(
    events: Flow<DashboardEvent>,
    navController: NavController,
    appViewModel: AppViewModel,
    executedShortcuts: Int,
    snackbarHostState: SnackbarHostState
) {
    val message = pluralStringResource(
        id = R.plurals.snackBar_recurring_shortcuts_executed,
        count = executedShortcuts,
        executedShortcuts
    )

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is DashboardEvent.NavigateTo -> navController.navigate(event.route)
            }
        }
    }

    LaunchedEffect(executedShortcuts) {
        if (executedShortcuts > 0) {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )

            appViewModel.clearScheduledShortcutsExecuted()
        }
    }
}