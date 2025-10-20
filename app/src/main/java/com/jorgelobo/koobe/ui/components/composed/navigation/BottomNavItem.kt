package com.jorgelobo.koobe.ui.components.composed.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val labelResId: Int,
    val icon: ImageVector
)