package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarConfig
import com.jorgelobo.koobe.ui.components.composed.appBar.CommonAppBar
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarAction
import com.jorgelobo.koobe.ui.components.composed.navigation.AppBottomNavigation
import com.jorgelobo.koobe.ui.components.composed.navigation.BottomNavigationDefaults
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme

@Composable
fun ShortcutManagerScreen(
    navController: NavController,
    config: ShortcutManagerConfig,
    viewModel: ShortcutManagerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ShortcutManagerEffects(
        navController = navController,
        viewModel = viewModel
    )

    Scaffold(
        topBar = {
            CommonAppBar(
                config = AppBarConfig(
                    headline = stringResource(R.string.headline_shortcut_manager),
                    leadingAction = AppBarAction(
                        icon = IconPack.BACK,
                        onClick = viewModel::onBackClick
                    ),
                    trailingActions = listOf(
                        AppBarAction(
                            icon = IconPack.FILTER,
                            onClick = {}
                        ),
                        AppBarAction(
                            icon = IconPack.ADD,
                            onClick = viewModel::onAddShortcutClick
                        )
                    )
                )
            )
        },
        bottomBar = {
            AppBottomNavigation(
                currentRoute = config.currentRoute,
                items = BottomNavigationDefaults.items,
                onItemSelected = { item -> config.onRouteSelected(item.route) }
            )
        },
        containerColor = AppTheme.colors.backgroundColors.screenBackground
    ) { padding ->
        ShortcutManagerScreenUI(
            modifier = Modifier.padding(padding),
            state = uiState,
            transactionTypeSelected = uiState.transactionTypeSelected,
            onTransactionTypeChanged = viewModel::onTransactionTypeChange,
            onEditShortcut = {},
            onDeleteShortcut = {}
            onEditShortcut = { viewModel.onEditShortcut(it) },
            onDeleteShortcut = { viewModel.onDeleteShortcutClick(it) }
        )
    }
}