package com.jorgelobo.koobe.ui.screen.categories.manager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

@Composable
fun CategoryManagerEffects(
    navController: NavController,
    viewModel: CategoryManagerViewModel
) {
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                CategoryManagerEvent.NavigateBack -> navController.popBackStack()

                is CategoryManagerEvent.NavigateTo -> navController.navigate(event.route)
            }
        }
    }
}