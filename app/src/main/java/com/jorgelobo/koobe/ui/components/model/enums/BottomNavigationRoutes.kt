package com.jorgelobo.koobe.ui.components.model.enums

import com.jorgelobo.koobe.ui.navigation.Route

enum class BottomNavigationRoutes(val route: String) {
    HOME(Route.Dashboard.route),
    HISTORIC(Route.Historic.route),
    REPORTS(Route.Reports.route),
    BUDGET_MANAGER(Route.BudgetManager.route),
    SETTINGS(Route.Settings.route)
}