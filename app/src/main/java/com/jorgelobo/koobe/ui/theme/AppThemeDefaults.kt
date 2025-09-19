package com.jorgelobo.koobe.ui.theme

import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.ui.theme.color.AppColorScheme
import com.jorgelobo.koobe.ui.theme.locals.LocalAppColors
import com.jorgelobo.koobe.ui.theme.locals.LocalAppShapes
import com.jorgelobo.koobe.ui.theme.locals.LocalAppTypography
import com.jorgelobo.koobe.ui.theme.shapes.AppShapes
import com.jorgelobo.koobe.ui.theme.typography.AppTypography

object AppTheme {
    val colors: AppColorScheme @Composable get() = LocalAppColors.current
    val typography: AppTypography @Composable get() = LocalAppTypography.current
    val shapes: AppShapes @Composable get() = LocalAppShapes.current
}