package com.jorgelobo.koobe.ui.screen.subcategories

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
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.theme.AppTheme

@Composable
fun SubcategoryEditorScreen(
    navController: NavController,
    config: SubcategoryEditorConfig,
    viewModel: SubcategoryEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SubcategoryEditorEffects(
        navController = navController,
        viewModel = viewModel
    )

    Scaffold(
        topBar = {
            CommonAppBar(
                config = AppBarConfig(
                    headline = stringResource(
                        uiState.headlineRes(config.isEditMode)
                    ),
                    leadingAction = AppBarAction(
                        icon = IconGeneral.CLOSE,
                        onClick = {}
                    ),
                    trailingActions = if (config.isEditMode) listOf(
                        AppBarAction(
                            IconGeneral.DELETE,
                            onClick = {}
                        )
                    ) else emptyList()
                )
            )
        },
        containerColor = AppTheme.colors.backgroundColors.screenBackground
    ) { padding ->
        SubcategoryEditorScreenUI(
            state = uiState,
            config = config,
            modifier = Modifier.padding(padding),
            onNameChanged = viewModel::onNameChanged,
            onIconSelectorClick = {},
            onCategoryChangeClick = {},
            onResetNameClick = viewModel::onResetName,
            onSaveClick = {}
        )
    }
}