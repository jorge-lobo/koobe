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
import com.jorgelobo.koobe.ui.screen.budgets.editor.BudgetEditorConfig
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorConfig
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorConfig

@Composable
fun CategorySelectorScreen(
    navController: NavController,
    config: CategorySelectorConfig,
    viewModel: CategorySelectorViewModel = hiltViewModel()
) {
    BackHandler {
        viewModel.onBackRequested()
    }

    LaunchedEffect(config) {
        viewModel.init(config)
    }

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
            navController.navigate(config.target.toRoute(config, uiState)) {
                popUpTo(Route.CategorySelector.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        },
        onCreateCategoryClick = {
            navController.navigate(
                Route.CategoryEditor.create(CategoryEditorConfig(categoryId = null))
            )
        }
    )
}

fun CategorySelectorTarget.toRoute(
    config: CategorySelectorConfig,
    uiState: CategorySelectorUiState
): String = when (this) {
    CategorySelectorTarget.TRANSACTION_EDITOR -> Route.TransactionEditor.create(
        TransactionEditorConfig(
            transactionId = config.transactionId,
            categoryId = uiState.selectedCategoryId,
            subcategoryId = uiState.selectedSubcategoryId,
            shortcutId = uiState.selectedShortcutId
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