package com.jorgelobo.koobe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jorgelobo.koobe.ui.app.AppViewModel
import com.jorgelobo.koobe.ui.screen.budgets.editor.BudgetEditorConfig
import com.jorgelobo.koobe.ui.screen.budgets.editor.BudgetEditorScreen
import com.jorgelobo.koobe.ui.screen.budgets.manager.BudgetManagerScreen
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorScreen
import com.jorgelobo.koobe.ui.screen.categories.manager.CategoryManagerScreen
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorScreen
import com.jorgelobo.koobe.ui.screen.dashboard.DashboardScreen
import com.jorgelobo.koobe.ui.screen.historic.HistoricConfig
import com.jorgelobo.koobe.ui.screen.historic.HistoricScreen
import com.jorgelobo.koobe.ui.screen.reports.ReportsScreen
import com.jorgelobo.koobe.ui.screen.settings.SettingsConfig
import com.jorgelobo.koobe.ui.screen.settings.SettingsScreen
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorConfig
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorScreen
import com.jorgelobo.koobe.ui.screen.shortcuts.manager.ShortcutManagerScreen
import com.jorgelobo.koobe.ui.screen.splash.SplashScreen
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorScreen
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorConfig
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorScreen
import kotlinx.serialization.json.Json

@Composable
fun NavGraph(
    navController: NavHostController,
    appViewModel: AppViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Route.Splash.route
    ) {
        // Splash
        composable(Route.Splash.route) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Route.Dashboard.route) {
                        popUpTo(Route.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Main views
        composable(Route.Dashboard.route) { DashboardScreen(navController) }
        composable(Route.Historic.route) {
            HistoricScreen(
                navController = navController,
                config = HistoricConfig(
                    currentRoute = Route.Historic.route,
                    onRouteSelected = { route ->
                        if (route != Route.Historic.route) {
                            navController.navigate(route) {
                                popUpTo(Route.Historic.route)
                                launchSingleTop = true
                            }
                        }
                    },
                )
            )
        }

        composable(Route.Reports.route) { ReportsScreen(navController) }

        composable(Route.Settings.route) {
            SettingsScreen(
                navController = navController,
                appViewModel = appViewModel,
                config = SettingsConfig(
                    currentRoute = Route.Settings.route,
                    onRouteSelected = { route ->
                        if (route != Route.Settings.route) {
                            navController.navigate(route) {
                                popUpTo(Route.Settings.route)
                                launchSingleTop = true
                            }
                        }
                    }
                )
            )
        }

        // Budgets
        composable(Route.BudgetManager.route) {
            BudgetManagerScreen(
                navController
            )
        }

        composable(
            route = "budget_editor/{config}",
            arguments = listOf(navArgument("config") { type = NavType.StringType })
        ) { backStackEntry ->
            val config = backStackEntry.decodeConfig<BudgetEditorConfig>()
            BudgetEditorScreen(navController, config)
        }

        // Categories
        composable(Route.CategoryManager.route) {
            CategoryManagerScreen(
                navController
            )
        }

        composable(
            route = "${Route.CategorySelector.route}/{config}",
            arguments = listOf(navArgument("config") { type = NavType.StringType }
            )
        ) { backstackEntry ->
            val config = backstackEntry.decodeConfig<CategorySelectorConfig>()
            CategorySelectorScreen(
                navController = navController,
                config = config
            )
        }

        composable(
            route = "category_editor/{config}",
            arguments = listOf(navArgument("config") { type = NavType.StringType })
        ) { backStackEntry ->
            val config = backStackEntry.decodeConfig<CategoryEditorConfig>()
            CategoryEditorScreen(navController, config)
        }

        // Subcategories
        composable(
            route = "subcategory_editor/{config}",
            arguments = listOf(navArgument("config") { type = NavType.StringType })
        ) { backStackEntry ->
            val config = backStackEntry.decodeConfig<SubcategoryEditorConfig>()
            SubcategoryEditorScreen(navController, config)
        }

        // Shortcuts
        composable(Route.ShortcutManager.route) { ShortcutManagerScreen(navController) }

        composable(
            route = "shortcut_editor/{config}",
            arguments = listOf(navArgument("config") { type = NavType.StringType })
        ) { backStackEntry ->
            val config = backStackEntry.decodeConfig<ShortcutEditorConfig>()
            ShortcutEditorScreen(navController, config)
        }

        // Transactions
        composable(
            route = "transaction_editor/{config}",
            arguments = listOf(navArgument("config") { type = NavType.StringType })
        ) { backStackEntry ->
            val config = backStackEntry.decodeConfig<TransactionEditorConfig>()
            TransactionEditorScreen(navController, config)
        }
    }
}

inline fun <reified T> NavBackStackEntry.decodeConfig(): T {
    val json = requireNotNull(arguments?.getString("config")) {
        "Argument config is missing"
    }
    return Json.decodeFromString(json)
}