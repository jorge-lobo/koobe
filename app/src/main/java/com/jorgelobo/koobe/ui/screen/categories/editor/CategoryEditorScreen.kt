package com.jorgelobo.koobe.ui.screen.categories.editor

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
import com.jorgelobo.koobe.ui.theme.AppTheme

@Composable
fun CategoryEditorScreen(
    navController: NavController,
    config: CategoryEditorConfig,
    viewModel: CategoryEditorViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            CommonAppBar(
                config = AppBarConfig(
                    headline = stringResource(uiState.headlineRes(config.isEditMode)),
                    leadingAction = AppBarAction(
                        icon = IconPack.CLOSE,
                        onClick = { }
                    ),
                    trailingActions = if (config.isEditMode) listOf(
                        if (uiState.isDeleteEnabled) {
                            AppBarAction(
                                icon = IconPack.DELETE,
                                onClick = { }
                            )
                        } else {
                            AppBarAction(
                                icon = IconPack.INFO,
                                onClick = { }
                            )
                        }
                    ) else emptyList()
                )
            )
        },
        containerColor = AppTheme.colors.backgroundColors.screenBackground
    ) { padding ->
        CategoryEditorScreenUI(
            state = uiState,
            config = config,
            modifier = Modifier.padding(padding),
            onNameChanged = {},
            onResetNameClick = {},
            onIconSelectorClick = { },
            onColorSelectorClick = { },
            onSaveClick = {},
            onAddSubcategoryClick = { },
            onEditSubcategory = { },
            onDeleteSubcategory = { },
            onTransactionTypeChange = { }
        )
    }
}