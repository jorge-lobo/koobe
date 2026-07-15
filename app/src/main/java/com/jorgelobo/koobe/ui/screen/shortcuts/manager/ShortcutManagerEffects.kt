package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

/**
 * Composable function that handles side effects for the Shortcut Manager screen.
 *
 * This function observes the event stream from the [ShortcutManagerViewModel] and executes
 * navigation actions, such as navigating back or moving to a specific route, using the provided
 * [NavController].
 *
 * @param navController The [NavController] used to perform navigation actions.
 * @param viewModel The [ShortcutManagerViewModel] that emits navigation events.
 */
@Composable
fun ShortcutManagerEffects(
    navController: NavController,
    viewModel: ShortcutManagerViewModel
) {
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                ShortcutManagerEvent.NavigateBack -> navController.popBackStack()

                is ShortcutManagerEvent.NavigateTo -> navController.navigate(event.route)
            }
        }
    }
}