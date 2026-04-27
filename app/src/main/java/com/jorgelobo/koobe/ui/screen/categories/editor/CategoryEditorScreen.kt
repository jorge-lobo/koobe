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
import com.jorgelobo.koobe.ui.screen.categories.editor.components.CategoryEditorActionButtons
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
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
        onDiscardDialogAction = {
            viewModel.onIntent(
                CategoryEditorIntent.Action.DiscardDialogAction(it)
            )
        },
        onDeleteDialogAction = {
            viewModel.onIntent(
                CategoryEditorIntent.Action.DeleteDialogAction(it)
            )
        },
        onInfoDialogClick = { viewModel.onIntent(CategoryEditorIntent.Action.HideInfoDialog) },
        onIconSelectorAction = {
            viewModel.onIntent(
                CategoryEditorIntent.Action.IconSelectorDialogAction(it)
            )
        },
        onColorSelectorAction = {
            viewModel.onIntent(
                CategoryEditorIntent.Action.ColorSelectorDialogAction(it)
            )
        }
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
                        onClick = { viewModel.onIntent(CategoryEditorIntent.Action.CloseClicked) }
                    ),
                    trailingActions = if (config.isEditMode) listOf(
                        if (uiState.isDeleteEnabled) {
                            AppBarAction(
                                icon = IconPack.DELETE,
                                onClick = {
                                    viewModel.onIntent(
                                        CategoryEditorIntent.Action.DeleteDialogAction(
                                            ConfirmationDialogAction.Open
                                        )
                                    )
                                }
                            )
                        } else {
                            AppBarAction(
                                icon = IconPack.INFO,
                                onClick = { viewModel.onIntent(CategoryEditorIntent.Action.ShowInfoDialog) }
                            )
                        }
                    ) else emptyList()
                )
            )
        },
        bottomBar = {
            CategoryEditorActionButtons(
                state = uiState,
                isEmpty = uiState.category.subcategories.isEmpty(),
                onAddSubcategoryClick = {
                    viewModel.onIntent(
                        CategoryEditorIntent.Action.SubcategoryEditionAction(null)
                    )
                },
                onSaveClick = { viewModel.onIntent(CategoryEditorIntent.Action.SaveClicked) }
            )
        },
        containerColor = AppTheme.colors.backgroundColors.screenBackground
    ) { padding ->
        CategoryEditorScreenUI(
            state = uiState,
            modifier = Modifier.padding(padding),
            onNameChanged = { viewModel.onIntent(CategoryEditorIntent.State.NameChanged(it)) },
            onResetNameClick = { viewModel.onIntent(CategoryEditorIntent.State.NameChanged("")) },
            onIconSelectorClick = {
                viewModel.onIntent(
                    CategoryEditorIntent.Action.IconSelectorDialogAction(
                        SelectorDialogAction.Open
                    )
                )
            },
            onColorSelectorClick = {
                viewModel.onIntent(
                    CategoryEditorIntent.Action.ColorSelectorDialogAction(
                        SelectorDialogAction.Open
                    )
                )
            },
            onAddSubcategoryClick = {
                viewModel.onIntent(
                    CategoryEditorIntent.Action.SubcategoryEditionAction(
                        null
                    )
                )
            },
            onEditSubcategory = {
                viewModel.onIntent(
                    CategoryEditorIntent.Action.SubcategoryEditionAction(it)
                )
            },
            onDeleteSubcategory = { id ->
                viewModel.onIntent(
                    CategoryEditorIntent.Action.RequestDeleteSubcategory(id)
                )
            },
            onTransactionTypeChange = {
                viewModel.onIntent(
                    CategoryEditorIntent.State.TypeSelected(it)
                )
            }
        )
    }
}