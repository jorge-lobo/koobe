package com.jorgelobo.koobe.ui.navigation

import android.net.Uri
import com.jorgelobo.koobe.ui.screen.budgets.editor.BudgetEditorConfig
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.categories.selector.CategorySelectorConfig
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorConfig
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorConfig
import kotlinx.serialization.json.Json

sealed class Route(val route: String) {
    data object Splash : Route("splash")
    data object Dashboard : Route("dashboard")
    data object Historic : Route("historic")
    data object Reports : Route("reports")
    data object Settings : Route("settings")

    // Budgets
    data object BudgetManager : Route("budget_manager")
    data object BudgetEditor : Route("budget_editor") {
        fun create(config: BudgetEditorConfig): String {
            val json = Json.encodeToString(config)
            return "$route/${Uri.encode(json)}"
        }
    }

    // Categories
    data object CategoryManager : Route("category_manager")
    data object CategorySelector : Route("category_selector") {
        fun create(config: CategorySelectorConfig): String {
            val json = Json.encodeToString(config)
            return "$route/${Uri.encode(json)}"
        }
    }

    data object CategoryEditor : Route("category_editor") {
        fun create(config: CategoryEditorConfig): String {
            val json = Json.encodeToString(config)
            return "$route/${Uri.encode(json)}"
        }
    }

    // Subcategories
    data object SubcategoryEditor : Route("subcategory_editor") {
        fun create(config: SubcategoryEditorConfig): String {
            val json = Json.encodeToString(config)
            return "$route/${Uri.encode(json)}"
        }
    }

    // Shortcuts
    data object ShortcutManager : Route("shortcut_manager")
    data object ShortcutEditor : Route("shortcut_editor") {
        fun create(config: ShortcutEditorConfig): String {
            val json = Json.encodeToString(config)
            return "$route/${Uri.encode(json)}"
        }
    }

    // Transactions
    data object TransactionEditor : Route("transaction_editor") {
        fun create(config: TransactionEditorConfig): String {
            val json = Json.encodeToString(config)
            return "$route/${Uri.encode(json)}"
        }
    }
}