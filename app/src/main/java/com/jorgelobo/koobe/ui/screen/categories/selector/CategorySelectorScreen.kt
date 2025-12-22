package com.jorgelobo.koobe.ui.screen.categories.selector

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun CategorySelectorScreen(
    navController: NavController,
    config: CategorySelectorConfig,
    viewModel: CategorySelectorViewModel = hiltViewModel()
) {
    LaunchedEffect(config) {
        viewModel.init(config)
    }

    val uiState by viewModel.uiState.collectAsState()

    CategorySelectorScreenUI(
        config = config,
        state = uiState,
        onBackClick = {},
        onSettingsClick = {},
        onTransactionTypeChange = viewModel::onTransactionTypeChanged,
        onCategorySelected = viewModel::onCategorySelected,
        onSubcategorySelected = viewModel::onSubcategorySelected,
        onShortcutSelected = viewModel::onShortcutSelected,
        onCategoryDetailSelected = viewModel::onCategoryDetailSelected,
        onChangeClick = {},
        onSubcategoryButtonClick = {},
        onShortcutButtonClick = {},
        onProceed = {}
    )
}