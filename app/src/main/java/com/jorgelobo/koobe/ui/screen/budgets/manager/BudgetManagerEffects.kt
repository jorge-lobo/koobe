package com.jorgelobo.koobe.ui.screen.budgets.manager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

/**
 * Composable function that handles navigation side effects for the Budget Manager screen.
 * It observes the event stream from the [viewModel] and performs the corresponding navigation
 * actions using the [navController].
 *
 * @param navController The navigation controller used to manage screen transitions.
 * @param viewModel The view model that emits [BudgetManagerEvent]s to be handled.
 */
@Composable
fun BudgetManagerEffects(
    navController: NavController,
    viewModel: BudgetManagerViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                BudgetManagerEvent.NavigateBack -> navController.popBackStack()

                is BudgetManagerEvent.NavigateTo -> navController.navigate(event.route)
            }
        }
    }
}