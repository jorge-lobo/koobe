package com.jorgelobo.koobe.ui.screen.shortcuts.manager

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
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

/**
 * Composable function that represents the Shortcut Manager screen.
 * This screen allows users to view, add, edit, delete, and sort their shortcuts.
 *
 * @param navController The navigation controller used to handle navigation events.
 * @param config Configuration object containing routing information and navigation callbacks.
 * @param viewModel The ViewModel that manages the UI state and business logic for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortcutManagerScreen(
    navController: NavController,
    config: ShortcutManagerConfig,
    viewModel: ShortcutManagerViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ShortcutManagerEffects(
        navController = navController,
        viewModel = viewModel
    )

    ShortcutManagerDialogs(
        state = uiState,
        sheetState = sheetState,
        onDeleteDialogAction = viewModel::onDeleteDialogAction,
        onSortingDialogAction = { viewModel.onSortingSheetAction(it) }
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
                            onClick = viewModel::onSortingClick
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
            onEditShortcut = { viewModel.onEditShortcut(it) },
            onDeleteShortcut = { viewModel.onDeleteShortcutClick(it) }
        )
    }
}