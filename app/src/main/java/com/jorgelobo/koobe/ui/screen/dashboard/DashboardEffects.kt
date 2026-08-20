package com.jorgelobo.koobe.ui.screen.dashboard

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.pluralStringResource
import androidx.navigation.NavController
import com.jorgelobo.koobe.R
import kotlinx.coroutines.flow.Flow

/**
 * Handles one-off side effects for the Dashboard screen.
 *
 * Observes dashboard events for navigation and displays a Snackbar when scheduled shortcuts have
 * been executed during application startup.
 *
 * The scheduled shortcut execution count is cleared after the Snackbar has been shown, preventing
 * the same feedback from being displayed again when the Dashboard screen is revisited.
 *
 * @param events Flow of one-off Dashboard events.
 * @param navController Controller used for Dashboard navigation.
 * @param executedShortcuts Number of scheduled shortcuts executed during application startup.
 * @param snackbarHostState Host state used to display the Snackbar.
 * @param onScheduledShortcutsFeedbackConsumed Callback to clear the scheduled shortcuts feedback.
 */
@Composable
fun DashboardEffects(
    events: Flow<DashboardEvent>,
    navController: NavController,
    executedShortcuts: Int,
    snackbarHostState: SnackbarHostState,
    onScheduledShortcutsFeedbackConsumed: () -> Unit
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

            onScheduledShortcutsFeedbackConsumed()
        }
    }
}