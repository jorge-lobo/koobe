package com.jorgelobo.koobe.ui.screen.subcategories

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
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

/**
 * Screen for creating or editing a subcategory.
 */
@Composable
fun SubcategoryEditorScreen(
    navController: NavController,
    config: SubcategoryEditorConfig,
    viewModel: SubcategoryEditorViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var currentSnackBarConfig by remember { mutableStateOf<SnackBarConfig?>(null) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SubcategoryEditorEffects(
        navController = navController,
        viewModel = viewModel,
        snackBarHostState = snackBarHostState,
        scope = scope,
        onSnackBarConfigChange = { currentSnackBarConfig = it }
    )

    SubcategoryEditorDialogs(
        state = uiState,
        onDiscardDialogAction = { viewModel.onDiscardDialogAction(it) },
        onDeleteDialogAction = { viewModel.onDeleteDialogAction(it) },
        onIconSelectorAction = { viewModel.onIconSelectorAction(it) },
        onInfoDialogClick = { viewModel.dismissInfoDialog() }
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) {
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
                    headline = stringResource(
                        uiState.headlineRes(config.isEditMode)
                    ),
                    leadingAction = AppBarAction(
                        icon = IconPack.CLOSE,
                        onClick = { viewModel.onCloseClick() }
                    ),
                    trailingActions = if (config.isEditMode) listOf(
                        if (uiState.isDeleteEnabled) {
                            AppBarAction(
                                IconPack.DELETE,
                                onClick = { viewModel.onDeleteDialogAction(ConfirmationDialogAction.Open) }
                            )
                        } else {
                            AppBarAction(
                                IconPack.INFO,
                                onClick = { viewModel.onInfoDialogOpen() }
                            )
                        }
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
            onIconSelectorClick = { viewModel.onIconSelectorAction(SelectorDialogAction.Open) },
            onCategoryChangeClick = {},
            onResetNameClick = viewModel::onResetName,
            onSaveClick = viewModel::onSaveClick,
        )
    }
}