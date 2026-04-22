package com.jorgelobo.koobe.ui.screen.categories.editor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import com.jorgelobo.koobe.ui.components.base.snackbar.AppSnackBar
import com.jorgelobo.koobe.ui.components.base.snackbar.SnackBarConfig
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarAction
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarConfig
import com.jorgelobo.koobe.ui.components.composed.appBar.CommonAppBar
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun CategoryEditorScreen(
    navController: NavController,
    config: CategoryEditorConfig,
    viewModel: CategoryEditorViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var currentSnackBarConfig by remember { mutableStateOf<SnackBarConfig?>(null) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CategoryEditorEffects(
        navController = navController,
        viewModel = viewModel,
        snackbarHostState = snackbarHostState,
        scope = scope,
        onSnackBarConfigChange = { currentSnackBarConfig = it }
    )

    CategoryEditorDialogs(
        state = uiState,
        onDiscardDialogAction = {},
        onDeleteDialogAction = {},
        onInfoDialogClick = {},
        onIconSelectorAction = {},
        onColorSelectorAction = {}
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                currentSnackBarConfig?.let { config ->
                    Box(modifier = Modifier.padding(Spacing.Medium)) {
                        AppSnackBar(
                            config = config,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
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