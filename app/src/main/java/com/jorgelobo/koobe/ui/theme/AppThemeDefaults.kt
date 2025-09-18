package com.jorgelobo.koobe.ui.theme

import androidx.compose.runtime.Composable

object AppTheme {
    val colors: AppColorScheme @Composable get() = LocalAppColors.current
    val typography: AppTypography @Composable get() = LocalAppTypography.current
    val shapes: AppShapes @Composable get() = LocalAppShapes.current
}