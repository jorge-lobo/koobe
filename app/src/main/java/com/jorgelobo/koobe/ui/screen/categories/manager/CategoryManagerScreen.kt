package com.jorgelobo.koobe.ui.screen.categories.manager

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import com.jorgelobo.koobe.ui.theme.AppTheme

@Composable
fun CategoryManagerScreen(
    navController: NavController,
    config: CategoryManagerConfig,
    viewModel: CategoryManagerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CommonAppBar(
                config = AppBarConfig(
                    headline = stringResource(R.string.headline_category_manager),
                    leadingAction = AppBarAction(
                        icon = IconGeneral.BACK,
                        onClick = {}
                    ),
                    trailingActions = listOf(
                        AppBarAction(
                            icon = IconGeneral.ADD,
                            onClick = {}
                        )
                    )
                )
            )
        },
        bottomBar = {
            AppBottomNavigation(
                currentRoute = config.currentRoute,
                items = BottomNavigationDefaults.items,
                onItemSelected = { item -> config.onRouteSelected(item.route)}
            )
        },
        containerColor = AppTheme.colors.backgroundColors.screenBackground
    ) { padding ->
        CategoryManagerScreenUI(
            modifier = Modifier.padding(padding),
            state = uiState,
            onTransactionTypeChange = {},
            onCategoryExpandToggle = {},
            onEditSubcategory = {},
            onDeleteSubcategory = {},
            onAddSubcategoryClick = {}
        )
    }
}