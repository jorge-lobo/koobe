package com.jorgelobo.koobe.ui.screen.subcategories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

@Composable
fun SubcategoryEditorEffects(
    navController: NavController,
    viewModel: SubcategoryEditorViewModel
) {
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                SubcategoryEditorEvent.NavigateBack -> navController.popBackStack()

                is SubcategoryEditorEvent.NavigateTo -> navController.navigate(event.route)
            }
        }
    }
}