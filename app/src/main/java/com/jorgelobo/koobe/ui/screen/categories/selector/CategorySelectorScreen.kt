package com.jorgelobo.koobe.ui.screen.categories.selector

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jorgelobo.koobe.ui.components.composed.dialogs.DiscardDialog
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.navigation.navigateClearingCurrent
import com.jorgelobo.koobe.ui.screen.budgets.editor.BudgetEditorConfig
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorConfig
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorConfig

/**
 * Entry point composable for the Category Selector feature.
 *
 * This screen is responsible for:
 * - Initializing the [CategorySelectorViewModel] with the provided [CategorySelectorConfig]
 * - Collecting UI state and one-off events from the ViewModel
 * - Handling system back navigation
 * - Delegating all user interactions to the ViewModel
 * - Performing navigation based on emitted [UiEvent]s and user actions
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
    // Intercepts system back presses and delegates handling to the ViewModel.
    BackHandler {
        viewModel.onBackRequested()
    }

    // Initializes the ViewModel once with the provided configuration.
    LaunchedEffect(config) {
        viewModel.init(config)
    }

    // Collects one-off UI events (such as navigation) emitted by the ViewModel.
    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                UiEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.showDiscardDialog) {
        DiscardDialog(
            onConfirm = viewModel::onDiscardConfirmed,
            onCancel = viewModel::onDiscardDialogDismiss
        )
    }

    CategorySelectorScreenUI(
        config = config,
        state = uiState,
        onBackClick = viewModel::onBackRequested,
        onTransactionTypeChange = viewModel::onTransactionTypeChanged,
        onCategorySelected = viewModel::onCategorySelected,
        onSubcategorySelected = viewModel::onSubcategorySelected,
        onShortcutSelected = viewModel::onShortcutSelected,
        onCategoryDetailSelected = viewModel::onCategoryDetailSelected,
        onChangeClick = viewModel::onChangeCategoryClick,
        onSubcategoryButtonClick = {
            navController.navigate(
                Route.SubcategoryEditor.create(
                    SubcategoryEditorConfig(
                        subcategoryId = uiState.selectedSubcategoryId,
                        categoryId = uiState.selectedCategoryId
                    )
                )
            )
        },
        onShortcutButtonClick = {
            navController.navigate(
                Route.ShortcutEditor.create(
                    ShortcutEditorConfig(
                        shortcutId = uiState.selectedShortcutId,
                        categoryId = uiState.selectedCategoryId
                    )
                )
            )
        },
        onProceed = {
            navController.navigateClearingCurrent(
                config.target.toRoute(config, uiState)
            )
        },
        onCreateCategoryClick = {
            navController.navigate(
                Route.CategoryEditor.create(CategoryEditorConfig(categoryId = null))
            )
        }
    )
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
            transactionType = config.initialTransactionType
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