package com.jorgelobo.koobe.ui.screen.historic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

/**
 * Composable function that handles navigation side effects for the Historic screen.
 * It observes the event stream from the [viewModel] and performs the corresponding navigation
 * actions using the [navController].
 *
 * @param navController The navigation controller used to manage screen transitions.
 * @param viewModel The view model that emits [HistoricEvent]s to be handled.
 */
@Composable
fun HistoricEffects(
    navController: NavController,
    viewModel: HistoricViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                HistoricEvent.NavigateBack -> navController.popBackStack()

                is HistoricEvent.NavigateTo -> navController.navigate(event.route)
            }
        }
    }
}