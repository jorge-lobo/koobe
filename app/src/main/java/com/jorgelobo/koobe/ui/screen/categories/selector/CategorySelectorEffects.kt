package com.jorgelobo.koobe.ui.screen.categories.selector

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.jorgelobo.koobe.ui.navigation.navigateClearingCurrent

/**
 * Composable function that handles the side effects for the Category Selector screen.
 *
 * This component manages:
 * 1. Initializing the [CategorySelectorViewModel] with the provided [CategorySelectorConfig].
 * 2. Collecting and reacting to UI events (navigation) emitted by the ViewModel's event stream.
 *
 * @param navController The navigation controller used to perform screen transitions.
 * @param config The configuration object containing initial data for the selector.
 * @param viewModel The state holder and business logic provider for the category selector.
 */
@Composable
fun CategorySelectorEffects(
    navController: NavController,
    config: CategorySelectorConfig,
    viewModel: CategorySelectorViewModel
) {
    // Initializes the ViewModel once with the provided configuration.
    LaunchedEffect(config) {
        viewModel.init(config)
    }

    // Collects one-off UI events (such as navigation) emitted by the ViewModel.
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                CategorySelectorEvent.NavigateBack -> navController.popBackStack()

                is CategorySelectorEvent.NavigateTo ->
                    navController.navigate(event.route)

                is CategorySelectorEvent.NavigateAndReplace ->
                    navController.navigateClearingCurrent(event.route)
            }
        }
    }
}