package com.jorgelobo.koobe.ui.components.model.icons

import androidx.compose.ui.graphics.vector.ImageVector
import com.adamglin.phosphoricons.RegularGroup
import com.adamglin.phosphoricons.regular.*

enum class IconBottomNavigation(val icon: ImageVector) {
    HOME(RegularGroup.HouseLine),
    HISTORIC(RegularGroup.ListDashes),
    REPORTS(RegularGroup.ChartLine),
    BUDGETS(RegularGroup.Gauge),
    SETTINGS(RegularGroup.GearSix)
}