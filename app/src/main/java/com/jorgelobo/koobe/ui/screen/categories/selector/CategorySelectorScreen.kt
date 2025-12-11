package com.jorgelobo.koobe.ui.screen.categories.selector

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun CategorySelectorScreen(
    navController: NavController,
    config: CategorySelectorConfig
) {

    CategorySelectorScreenUI(
        config = config,
        onBackClick = {},
        categories = emptyList(),
        onProceedToNext = {}
    )
}