package com.jorgelobo.koobe.ui.screen.categories.selector

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarAction
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarConfig
import com.jorgelobo.koobe.ui.components.composed.appBar.CommonAppBar
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.screen.budgets.editor.BudgetEditorConfig
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorConfig
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorConfig
import com.jorgelobo.koobe.ui.theme.AppTheme

/**
 * Entry point composable for the Category Selector feature.
 *
 * This screen is responsible for:
 * - Initializing the [CategorySelectorViewModel] with the provided [CategorySelectorConfig]
 * - Collecting UI state and one-off events from the ViewModel
 * - Handling system back navigation
 * - Delegating all user interactions to the ViewModel
 *
 * The composable itself contains no business logic and acts purely as a state-to-UI and
 * event-to-navigation bridge.
 */
@Composable
fun CategorySelectorScreen(
    navController: NavController,
    config: CategorySelectorConfig,
    viewModel: CategorySelectorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onBack = rememberUpdatedState(viewModel::onBackRequested)
    val mode = config.mode

    // Intercepts system back presses and delegates handling to the ViewModel.
    BackHandler {
        onBack.value()
    }

    CategorySelectorEffects(
        navController = navController,
        config = config,
        viewModel = viewModel
    )

    CategorySelectorDialogs(
        state = uiState,
        onDiscardDialogAction = viewModel::onDiscardDialogAction
    )

    Scaffold(
        topBar = {
            CommonAppBar(
                config = AppBarConfig(
                    headline = stringResource(uiState.headlineRes),
                    leadingAction = AppBarAction(mode.leadingIcon) { viewModel::onBackRequested },
                    trailingActions = emptyList()
                )
            )
        },
        containerColor = AppTheme.colors.backgroundColors.screenBackground
    ) { innerPadding ->
        CategorySelectorScreenUI(
            modifier = Modifier.padding(innerPadding),
            config = config,
            state = uiState,
            onTransactionTypeChange = viewModel::onTransactionTypeChanged,
            onCategorySelected = viewModel::onCategorySelected,
            onSubcategorySelected = viewModel::onSubcategorySelected,
            onShortcutSelected = viewModel::onShortcutSelected,
            onCategoryDetailSelected = viewModel::onCategoryDetailSelected,
            onChangeClick = viewModel::onChangeCategoryClick,
            onSubcategoryButtonClick = viewModel::onSubcategoryEditorRequested,
            onShortcutButtonClick = viewModel::onShortcutEditorRequested,
            onProceed = viewModel::onProceedRequested,
            onCreateCategoryClick = viewModel::onCreateCategoryRequested,
        )
    }
}

/**
 * Resolves the navigation route for the given [CategorySelectorTarget] using the current
 * selector configuration and UI state.
 *
 * This function centralizes the mapping between selection results and their corresponding
 * destination routes.
 */
fun CategorySelectorTarget.toRoute(
    config: CategorySelectorConfig,
    uiState: CategorySelectorUiState
): String = when (this) {
    CategorySelectorTarget.TRANSACTION_EDITOR -> Route.TransactionEditor.create(
        TransactionEditorConfig(
            transactionId = config.transactionId,
            categoryId = uiState.selectedCategoryId!!,
            subcategoryId = uiState.selectedSubcategoryId,
            shortcutId = uiState.selectedShortcutId,
            transactionType = config.initialTransactionType,
            originRoute = Route.Dashboard.route
        )
    )

    CategorySelectorTarget.SHORTCUT_EDITOR -> Route.ShortcutEditor.create(
        ShortcutEditorConfig(
            shortcutId = config.shortcutId,
            categoryId = uiState.selectedCategoryId
        )
    )

    CategorySelectorTarget.SUBCATEGORY_EDITOR -> Route.SubcategoryEditor.create(
        SubcategoryEditorConfig(
            subcategoryId = config.subcategoryId,
            categoryId = uiState.selectedCategoryId
        )
    )

    CategorySelectorTarget.BUDGET_EDITOR -> Route.BudgetEditor.create(
        BudgetEditorConfig(
            budgetId = config.budgetId,
            categoryId = uiState.selectedCategoryId,
            subcategoryId = uiState.selectedSubcategoryId
        )
    )
}