package com.jorgelobo.koobe.ui.screen.categories.selector

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.jorgelobo.koobe.ui.navigation.Route

@Composable
fun CategorySelectorScreen(
    navController: NavController,
    config: CategorySelectorConfig,
    viewModel: CategorySelectorViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.init(config)
    }

    val uiState by viewModel.uiState.collectAsState()

    val route = config.target.toRoute()

    CategorySelectorScreenUI(
        config = config,
        state = uiState,
        onBackClick = { navController.popBackStack() },
        onSettingsClick = { navController.navigate(Route.Settings.route) },
        onTransactionTypeChange = viewModel::onTransactionTypeChanged,
        onCategorySelected = viewModel::onCategorySelected,
        onSubcategorySelected = viewModel::onSubcategorySelected,
        onShortcutSelected = viewModel::onShortcutSelected,
        onCategoryDetailSelected = viewModel::onCategoryDetailSelected,
        onChangeClick = viewModel::onChangeCategoryClick,
        onSubcategoryButtonClick = { navController.navigate(Route.SubcategoryEditor.create(0)) },
        onShortcutButtonClick = { navController.navigate(Route.ShortcutEditor.create(0)) },
        onProceed = { navController.navigate(route) }
    )
}

fun CategorySelectorTarget.toRoute(): String = when (this) {
    CategorySelectorTarget.TRANSACTION_EDITOR -> Route.TransactionEditor.create(0)
    CategorySelectorTarget.SHORTCUT_EDITOR -> Route.ShortcutEditor.create(0)
    CategorySelectorTarget.SUBCATEGORY_EDITOR -> Route.SubcategoryEditor.create(0)
    CategorySelectorTarget.BUDGET_EDITOR -> Route.BudgetEditor.create(0)
}