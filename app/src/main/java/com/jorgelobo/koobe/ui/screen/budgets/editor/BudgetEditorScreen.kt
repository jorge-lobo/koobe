package com.jorgelobo.koobe.ui.screen.budgets.editor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jorgelobo.koobe.ui.components.base.snackbar.SnackBarConfig
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarAction
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarConfig
import com.jorgelobo.koobe.ui.components.composed.appBar.CommonAppBar
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.budgets.editor.components.BudgetEditorSaveButton
import com.jorgelobo.koobe.ui.theme.AppTheme

/**
 * Composable screen that allows users to create or edit budgets.
 *
 * This screen provides the user interface for inputting budget details, including amount, category,
 * and recurrence settings. It handles both the creation of new budgets and the modification of
 * existing ones based on the provided configuration.
 *
 * @param navController The navigation controller used to handle screen transitions.
 *  * @param config Configuration identifying whether the screen is in create or edit mode.
 *  * @param viewModel The business logic handler for managing shortcut data and user intents.
 */
@Composable
fun BudgetEditorScreen(
    navController: NavController,
    config: BudgetEditorConfig,
    viewModel: BudgetEditorViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var currentSnackbarConfig by remember { mutableStateOf<SnackBarConfig?>(null) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.onIntent(BudgetEditorIntent.Action.CloseClicked)
    }

    BudgetEditorEffects(
        navController = navController,
        viewModel = viewModel,
        snackBarHostState = snackBarHostState,
        scope = scope,
        onSnackBarConfigChange = { currentSnackbarConfig = it }
    )

    BudgetEditorDialogs(
        state = uiState,
        onDiscardDialogAction = {
            viewModel.onIntent(BudgetEditorIntent.Action.DiscardDialogUpdated(it))
        },
        onDeleteDialogAction = {
            viewModel.onIntent(BudgetEditorIntent.Action.DeleteDialogUpdated(it))
        },
        onDuplicateDialogAction = {
            viewModel.onIntent(BudgetEditorIntent.Action.DuplicateBudgetDialogUpdated(it))
        },
        onContinueCreatingBudget = {
            viewModel.onIntent(BudgetEditorIntent.Action.ContinueCreatingBudget)
        },
        onCancelExistingBudgetsDialog = {
            viewModel.onIntent(BudgetEditorIntent.Action.CancelExistingBudgetsDialog)
        },
        onExistingBudgetClick = {
            viewModel.onIntent(BudgetEditorIntent.Action.EditExistingBudget(it.budget.id))
        }
    )

    Scaffold(
        topBar = {
            CommonAppBar(
                config = AppBarConfig(
                    headline = stringResource(
                        uiState.headlineRes()
                    ),
                    leadingAction = AppBarAction(
                        icon = IconPack.CLOSE,
                        onClick = { viewModel.onIntent(BudgetEditorIntent.Action.CloseClicked) }
                    ),
                    trailingActions = when (config) {
                        is BudgetEditorConfig.Create -> emptyList()
                        is BudgetEditorConfig.Edit -> listOf(
                            AppBarAction(
                                IconPack.DELETE,
                                onClick = { viewModel.onIntent(BudgetEditorIntent.Action.RequestDeleteBudget) }
                            )
                        )
                    }
                )
            )
        },
        bottomBar = {
            BudgetEditorSaveButton(
                state = uiState,
                onSaveClick = { viewModel.onIntent(BudgetEditorIntent.Action.SaveClicked) }
            )
        },
        containerColor = AppTheme.colors.backgroundColors.screenBackground
    ) { padding ->
        BudgetEditorScreenUI(
            state = uiState,
            modifier = Modifier.padding(padding),
            isRepeatChecked = uiState.isRepeat,
            onChangeCategoryClick = { viewModel.onIntent(BudgetEditorIntent.Action.ChangeCategoryClicked) },
            onChangePeriodClick = { viewModel.onIntent(BudgetEditorIntent.State.PeriodChanged(it)) },
            onCheckedChange = { viewModel.onIntent(BudgetEditorIntent.State.RepeatChanged(it)) },
            onResetAmountClick = { viewModel.onIntent(BudgetEditorIntent.State.AmountResetClicked) },
            onKeyClick = { viewModel.onIntent(BudgetEditorIntent.State.AmountKeyPressed(it)) },
        )
    }
}