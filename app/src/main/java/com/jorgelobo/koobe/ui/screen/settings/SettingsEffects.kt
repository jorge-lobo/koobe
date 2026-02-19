package com.jorgelobo.koobe.ui.screen.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

/**
 * Composable function that handles side effects for the Settings screen.
 * It listens to navigation events emitted by the [SettingsViewModel] and performs
 * the corresponding actions using the [NavController].
 *
 * @param navController The navigation controller used to perform screen transitions.
 * @param viewModel The view model that provides the stream of settings-related events.
 */
@Composable
fun SettingsEffects(
    navController: NavController,
    viewModel: SettingsViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                SettingsEvent.NavigateBack -> navController.popBackStack()

                is SettingsEvent.NavigateTo -> navController.navigate(event.route)
            }
        }
    }
}