package com.jorgelobo.koobe.ui.components.composed.navigation

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.model.enums.BottomNavigationRoutes
import com.jorgelobo.koobe.ui.components.model.icons.IconBottomNavigation

object BottomNavigationDefaults {
    val items = listOf(
        BottomNavItem(
            BottomNavigationRoutes.HOME.name,
            R.string.nav_home,
            IconBottomNavigation.HOME.icon
        ),
        BottomNavItem(
            BottomNavigationRoutes.HISTORIC.name,
            R.string.nav_historic,
            IconBottomNavigation.HISTORIC.icon
        ),
        BottomNavItem(
            BottomNavigationRoutes.REPORTS.name,
            R.string.nav_reports,
            IconBottomNavigation.REPORTS.icon
        ),
        BottomNavItem(
            BottomNavigationRoutes.BUDGET_MANAGER.name,
            R.string.nav_budgets,
            IconBottomNavigation.BUDGETS.icon
        ),
        BottomNavItem(
            BottomNavigationRoutes.SETTINGS.name,
            R.string.nav_settings,
            IconBottomNavigation.SETTINGS.icon
        ),
    )
}