package com.jorgelobo.koobe.ui.components.composed.navigation

import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class BottomNavItem(
    val route: String,
    val labelResId: Int,
    val icon: IconPack
)