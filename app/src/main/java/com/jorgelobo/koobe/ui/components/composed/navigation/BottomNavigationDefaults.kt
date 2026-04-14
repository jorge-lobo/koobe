package com.jorgelobo.koobe.ui.components.composed.navigation

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.model.enums.BottomNavigationRoutes
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

object BottomNavigationDefaults {
    val items = listOf(
        BottomNavItem(
            BottomNavigationRoutes.HOME.route,
            R.string.nav_home,
            IconPack.HOUSE_LINE
        ),
        BottomNavItem(
            BottomNavigationRoutes.HISTORIC.route,
            R.string.nav_historic,
            IconPack.LIST_DASHES
        ),
        BottomNavItem(
            BottomNavigationRoutes.REPORTS.route,
            R.string.nav_reports,
            IconPack.CHART_LINE
        ),
        BottomNavItem(
            BottomNavigationRoutes.BUDGET_MANAGER.route,
            R.string.nav_budgets,
            IconPack.GAUGE
        ),
        BottomNavItem(
            BottomNavigationRoutes.SETTINGS.route,
            R.string.nav_settings,
            IconPack.GEAR_SIX
        ),
    )
}