package com.jorgelobo.koobe.ui.navigation

sealed class Route(val route: String) {
    data object Splash : Route("splash")
    data object Dashboard : Route("dashboard")
    data object Historic : Route("historic")
    data object Reports : Route("reports")
    data object Settings : Route("settings")

    // Budgets
    data object BudgetManager : Route("budget_manager")
    data object BudgetEditor : Route("budget_editor/{id}") {
        fun create(id: Int) = "budget_editor/$id"
    }

    // Categories
    data object CategorySelector : Route("category_selector")
    data object CategoryManager : Route("category_manager")
    data object CategoryEditor : Route("category_editor/{id}") {
        fun create(id: Int) = "category_editor/$id"
    }

    // Subcategories
    data object SubcategoryEditor : Route("subcategory_editor/{id}") {
        fun create(id: Int) = "subcategory_editor/$id"
    }

    // Shortcuts
    data object ShortcutManager : Route("shortcut_manager")
    data object ShortcutEditor : Route("shortcut_editor/{id}") {
        fun create(id: Int) = "shortcut_editor/$id"
    }

    // Transactions
    data object TransactionEditor : Route("transaction_editor/{id}") {
        fun create(id: Int) = "transaction_editor/$id"
    }
}