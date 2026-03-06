package com.jorgelobo.koobe.ui.screen.historic

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
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarAction
import com.jorgelobo.koobe.ui.components.composed.appBar.AppBarConfig
import com.jorgelobo.koobe.ui.components.composed.appBar.CommonAppBar
import com.jorgelobo.koobe.ui.components.composed.navigation.AppBottomNavigation
import com.jorgelobo.koobe.ui.components.composed.navigation.BottomNavigationDefaults
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.periodFilter.PeriodFilterAction
import com.jorgelobo.koobe.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricScreen(
    navController: NavController,
    config: HistoricConfig,
    viewModel: HistoricViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()

    HistoricEffects(
        navController = navController,
        viewModel = viewModel
    )

    HistoricDialogs(
        state = uiState,
        sheetState = sheetState,
        onPeriodFilterAction = viewModel::onPeriodFilterAction,
        onDatePickerDialogAction = viewModel::onDatePickerDialogAction
    )

    Scaffold(
        topBar = {
            CommonAppBar(
                config = AppBarConfig(
                    headline = stringResource(R.string.headline_historic),
                    leadingAction = AppBarAction(
                        icon = IconGeneral.BACK,
                        onClick = viewModel::onBackClick
                    ),
                    trailingActions = listOf(
                        AppBarAction(
                            icon = IconGeneral.FILTER,
                            onClick = { viewModel.onPeriodFilterAction(PeriodFilterAction.Open) }
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
        HistoricScreenUI(
            modifier = Modifier.padding(padding),
            state = uiState,
            onTransactionTypeChange = viewModel::onTransactionTypeChanged,
            onCategoryExpandToggle = viewModel::onCategoryExpandToggle,
            onSubcategoryExpandToggle = viewModel::onSubcategoryExpandToggle,
            onTransactionClick = viewModel::onTransactionItemClick
        )
    }
}