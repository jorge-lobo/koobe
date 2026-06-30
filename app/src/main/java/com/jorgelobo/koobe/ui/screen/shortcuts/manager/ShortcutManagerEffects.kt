package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

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