package com.jorgelobo.koobe.ui.screen.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
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
) {
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is DashboardEvent.NavigateTo -> navController.navigate(event.route)
            }
        }
    }
}