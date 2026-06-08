package com.jorgelobo.koobe.ui.screen.shortcuts.editor

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
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.components.ShortcutSaveSection
import com.jorgelobo.koobe.ui.theme.AppTheme

@Composable
fun ShortcutEditorScreen(
    navController: NavController,
    config: ShortcutEditorConfig,
    viewModel: ShortcutEditorViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = { },
        topBar = {
            CommonAppBar(
                config = AppBarConfig(
                    headline = stringResource(uiState.headlineRes),
                    leadingAction = AppBarAction(
                        icon = IconPack.CLOSE,
                        onClick = {}
                    ),
                    trailingActions = buildList {
                        add(
                            AppBarAction(
                                IconPack.CHANGE,
                                onClick = {}
                            )
                        )

                        if (config is ShortcutEditorConfig.Edit) {
                            add(
                                AppBarAction(
                                    IconPack.DELETE,
                                    onClick = {}
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
                onSaveClick = {}
            )
        },
        containerColor = AppTheme.colors.backgroundColors.screenBackground
    ) { padding ->
        ShortcutEditorScreenUI(
            state = uiState,
            modifier = Modifier.padding(padding),
            onIconSelectorClick = {},
            onNameChanged = {},
            onResetNameClick = {},
            isChecked = uiState.repeat,
            onCheckedChange = {},
            onPeriodSelectorClick = {},
            onResetAmountClick = {},
            onPaymentSelectorClick = {},
            onCurrencySelectorClick = {},
            onKeyClick = {}
        )
    }
}