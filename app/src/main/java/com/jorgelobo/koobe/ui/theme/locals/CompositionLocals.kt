package com.jorgelobo.koobe.ui.theme.locals

import androidx.compose.runtime.staticCompositionLocalOf
import com.jorgelobo.koobe.ui.theme.color.AppColorScheme
import com.jorgelobo.koobe.ui.theme.shapes.AppShapes
import com.jorgelobo.koobe.ui.theme.typography.AppTypography

val LocalAppColors = staticCompositionLocalOf<AppColorScheme> {
    error("No AppColorScheme provided - wrap your composable in KoobeTheme")
}

val LocalAppTypography = staticCompositionLocalOf<AppTypography> {
    error("No AppTypography provided - wrap your composable in KoobeTheme")
}

val LocalAppShapes = staticCompositionLocalOf<AppShapes> {
    error("No AppShapes provided - wrap your composable in KoobeTheme")
}