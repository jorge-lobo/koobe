package com.jorgelobo.koobe.ui.screen.budgets.editor

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
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

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CommonAppBar(
                config = AppBarConfig(
                    headline = stringResource(
                        uiState.headlineRes()
                    ),
                    leadingAction = AppBarAction(
                        icon = IconPack.CLOSE,
                        onClick = {}
                    ),
                    trailingActions = if (config.isEditMode) listOf(
                        AppBarAction(
                            IconPack.DELETE,
                            onClick = {}
                        )
                    ) else emptyList()
                )
            )
        },
        bottomBar = {
            BudgetEditorSaveButton(
                state = uiState,
                onSaveClick = {}
            )
        },
        containerColor = AppTheme.colors.backgroundColors.screenBackground
    ) { padding ->
        BudgetEditorScreenUI(
            state = uiState,
            modifier = Modifier.padding(padding),
            isRepeatChecked = uiState.isRepeat,
            onChangeCategoryClick = {},
            onChangePeriodClick = {},
            onCheckedChange = {},
            onResetAmountClick = {},
            onKeyClick = {},
        )
    }
}