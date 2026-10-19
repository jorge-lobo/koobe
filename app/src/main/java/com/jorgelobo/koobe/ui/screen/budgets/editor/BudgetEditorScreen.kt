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
                    trailingActions = if (config.isEditMode) listOf(
                        AppBarAction(
                            IconPack.DELETE,
                            onClick = { viewModel.onIntent(BudgetEditorIntent.Action.RequestDeleteBudget) }
                        )
                    ) else emptyList()
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