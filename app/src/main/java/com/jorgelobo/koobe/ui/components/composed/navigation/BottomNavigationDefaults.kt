package com.jorgelobo.koobe.ui.components.composed.navigation

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.model.icons.IconBottomNavigation

object BottomNavigationDefaults {
    val items = listOf(
        BottomNavItem("home", R.string.nav_home, IconBottomNavigation.HOME.icon),
        BottomNavItem("historic", R.string.nav_historic, IconBottomNavigation.HISTORIC.icon),
        BottomNavItem("reports", R.string.nav_reports, IconBottomNavigation.REPORTS.icon),
        BottomNavItem("budgets", R.string.nav_budgets, IconBottomNavigation.BUDGETS.icon),
        BottomNavItem("settings", R.string.nav_settings, IconBottomNavigation.SETTINGS.icon),
    )
}