package com.jorgelobo.koobe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jorgelobo.koobe.ui.screen.budgets.BudgetEditorScreen
import com.jorgelobo.koobe.ui.screen.budgets.BudgetManagerScreen
import com.jorgelobo.koobe.ui.screen.categories.CategoryEditorScreen
import com.jorgelobo.koobe.ui.screen.categories.CategoryManagerScreen
import com.jorgelobo.koobe.ui.screen.categories.CategorySelectorScreen
import com.jorgelobo.koobe.ui.screen.dashboard.DashboardScreen
import com.jorgelobo.koobe.ui.screen.historic.HistoricScreen
import com.jorgelobo.koobe.ui.screen.reports.ReportsScreen
import com.jorgelobo.koobe.ui.screen.settings.SettingsScreen
import com.jorgelobo.koobe.ui.screen.shortcuts.ShortcutEditorScreen
import com.jorgelobo.koobe.ui.screen.shortcuts.ShortcutManagerScreen
import com.jorgelobo.koobe.ui.screen.splash.SplashScreen
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorScreen
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorScreen

@Composable
fun NavGraph(
    navController: NavHostController
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
        composable(Route.Historic.route) { HistoricScreen(navController) }
        composable(Route.Reports.route) { ReportsScreen(navController) }
        composable(Route.Settings.route) { SettingsScreen(navController) }

        // Budgets
        composable(Route.BudgetManager.route) { BudgetManagerScreen(navController) }

        composable(
            Route.BudgetEditor.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.intArg("id")
            BudgetEditorScreen(navController, id)
        }

        // Categories
        composable(Route.CategorySelector.route) { CategorySelectorScreen(navController) }
        composable(Route.CategoryManager.route) { CategoryManagerScreen(navController) }

        composable(
            Route.CategoryEditor.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.intArg("id")
            CategoryEditorScreen(navController, id)
        }

        // Subcategories
        composable(
            Route.SubcategoryEditor.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.intArg("id")
            SubcategoryEditorScreen(navController, id)
        }

        // Shortcuts
        composable(Route.ShortcutManager.route) { ShortcutManagerScreen(navController) }

        composable(
            Route.ShortcutEditor.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.intArg("id")
            ShortcutEditorScreen(navController, id)
        }

        // Transactions
        composable(
            Route.TransactionEditor.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.intArg("id")
            TransactionEditorScreen(navController, id)
        }
    }
}

fun NavBackStackEntry.intArg(key: String): Int =
    arguments!!.getInt(key)