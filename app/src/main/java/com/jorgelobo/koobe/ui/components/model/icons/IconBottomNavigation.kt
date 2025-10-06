package com.jorgelobo.koobe.ui.components.model.icons

import androidx.compose.ui.graphics.vector.ImageVector
import com.adamglin.phosphoricons.RegularGroup
import com.adamglin.phosphoricons.regular.ChartLine
import com.adamglin.phosphoricons.regular.Gauge
import com.adamglin.phosphoricons.regular.GearSix
import com.adamglin.phosphoricons.regular.HouseLine
import com.adamglin.phosphoricons.regular.ListDashes

enum class IconBottomNavigation(val icon: ImageVector) {
    HOME(RegularGroup.HouseLine),
    HISTORIC(RegularGroup.ListDashes),
    REPORTS(RegularGroup.ChartLine),
    BUDGETS(RegularGroup.Gauge),
    SETTINGS(RegularGroup.GearSix)
}