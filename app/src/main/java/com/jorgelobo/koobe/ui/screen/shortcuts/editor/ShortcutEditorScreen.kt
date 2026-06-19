package com.jorgelobo.koobe.ui.screen.shortcuts.editor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.components.ShortcutSaveSection
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortcutEditorScreen(
    navController: NavController,
    config: ShortcutEditorConfig,
    viewModel: ShortcutEditorViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var currentSnackbarConfig by remember { mutableStateOf<SnackBarConfig?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler { viewModel.onIntent(ShortcutEditorIntent.Action.CloseClicked) }

    ShortcutEditorEffects(
        navController = navController,
        viewModel = viewModel,
        snackbarHostState = snackbarHostState,
        scope = scope,
        onSnackBarConfigChange = { currentSnackbarConfig = it }
    )

    ShortcutEditorDialogs(
        state = uiState,
        sheetState = sheetState,
        onDiscardDialogAction = {
            viewModel.onIntent(
                ShortcutEditorIntent.Action.DiscardDialogUpdated(
                    it
                )
            )
        },
        onDeleteDialogAction = {
            viewModel.onIntent(
                ShortcutEditorIntent.Action.DeleteDialogUpdated(
                    it
                )
            )
        },
        onIconSelectorDialogAction = {
            viewModel.onIntent(
                ShortcutEditorIntent.Action.IconSelectorDialogUpdated(
                    it
                )
            )
        },
        onCurrencyDialogAction = {
            viewModel.onIntent(
                ShortcutEditorIntent.Action.CurrencyDialogUpdated(
                    it
                )
            )
        },
        onPeriodSelectorAction = {
            viewModel.onIntent(
                ShortcutEditorIntent.Action.PeriodSelectorUpdated(
                    it
                )
            )
        },
        onPaymentMethodSelectorAction = {
            viewModel.onIntent(
                ShortcutEditorIntent.Action.PaymentMethodSelectorUpdated(
                    it
                )
            )
        }
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                currentSnackbarConfig?.let { config ->
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
                    headline = stringResource(uiState.headlineRes),
                    leadingAction = AppBarAction(
                        icon = IconPack.CLOSE,
                        onClick = { viewModel.onIntent(ShortcutEditorIntent.Action.CloseClicked) }
                    ),
                    trailingActions = buildList {
                        add(
                            AppBarAction(
                                IconPack.CHANGE,
                                onClick = { viewModel.onIntent(ShortcutEditorIntent.Action.ChangeCategoryClicked) }
                            )
                        )

                        if (config is ShortcutEditorConfig.Edit) {
                            add(
                                AppBarAction(
                                    IconPack.DELETE,
                                    onClick = { viewModel.onIntent(ShortcutEditorIntent.Action.RequestDeleteShortcut) }
                                )
                            )
                        }
                    }
                )
            )
        },
        bottomBar = {
            ShortcutSaveSection(
                state = uiState,
                onSaveClick = { viewModel.onIntent(ShortcutEditorIntent.Action.SaveClicked) }
            )
        },
        containerColor = AppTheme.colors.backgroundColors.screenBackground
    ) { padding ->
        ShortcutEditorScreenUI(
            state = uiState,
            modifier = Modifier.padding(padding),
            onIconSelectorClick = {
                viewModel.onIntent(
                    ShortcutEditorIntent.Action.IconSelectorDialogUpdated(
                        SelectorDialogAction.Open
                    )
                )
            },
            onNameChanged = { viewModel.onIntent(ShortcutEditorIntent.State.NameChanged(it)) },
            onResetNameClick = { viewModel.onIntent(ShortcutEditorIntent.State.NameChanged("")) },
            isChecked = uiState.repeat,
            onCheckedChange = { viewModel.onIntent(ShortcutEditorIntent.State.RepeatChanged(it)) },
            onPeriodSelectorClick = {
                viewModel.onIntent(
                    ShortcutEditorIntent.Action.PeriodSelectorUpdated(
                        SelectorSheetAction.Open
                    )
                )
            },
            onResetAmountClick = { viewModel.onIntent(ShortcutEditorIntent.State.AmountResetClicked) },
            onPaymentSelectorClick = {
                viewModel.onIntent(
                    ShortcutEditorIntent.Action.PaymentMethodSelectorUpdated(
                        SelectorSheetAction.Open
                    )
                )
            },
            onCurrencySelectorClick = {
                viewModel.onIntent(
                    ShortcutEditorIntent.Action.CurrencyDialogUpdated(
                        SelectorDialogAction.Open
                    )
                )
            },
            onKeyClick = { viewModel.onIntent(ShortcutEditorIntent.State.AmountKeyPressed(it)) }
        )
    }
}