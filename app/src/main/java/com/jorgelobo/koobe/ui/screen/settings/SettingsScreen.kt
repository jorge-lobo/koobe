package com.jorgelobo.koobe.ui.screen.settings

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
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.navigation.Route
import com.jorgelobo.koobe.ui.theme.AppTheme

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel(),
    config: SettingsConfig
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CommonAppBar(
                config = AppBarConfig(
                    headline = stringResource(R.string.headline_settings),
                    leadingAction = AppBarAction(
                        icon = IconGeneral.BACK,
                        onClick = { navController.popBackStack() }
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
        SettingsScreenUI(
            modifier = Modifier.padding(padding),
            config = SettingsConfig(
                themeSelected = uiState.themeSelected,
                languageSelected = uiState.languageSelected,
                currencySelected = uiState.currencySelected,
                daySelected = uiState.startOfWeekSelected,
                paymentMethodSelected = uiState.paymentMethodSelected,
                currentRoute = config.currentRoute,
                onRouteSelected = config.onRouteSelected
            ),
            onThemeOptionChange = {/* viewModel.onThemeOptionChange(it) */},
            onLanguageSelectorClick = {},
            onCurrencySelectorClick = {},
            onStartOfWeekSelectorClick = {},
            onPaymentMethodSelectorClick = {},
            onManageCategoriesClick = { navController.navigate(Route.CategoryManager.route) },
            onManageShortcutsClick = { navController.navigate(Route.ShortcutManager.route) }
        )
    }
}