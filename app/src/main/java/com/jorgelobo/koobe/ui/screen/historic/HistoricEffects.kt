package com.jorgelobo.koobe.ui.screen.historic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

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