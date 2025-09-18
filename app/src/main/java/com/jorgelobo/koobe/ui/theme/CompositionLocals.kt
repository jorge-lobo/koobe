package com.jorgelobo.koobe.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppColors = staticCompositionLocalOf<AppColorScheme> {
    error("No AppColorScheme provided - wrap your composable in KoobeTheme")
}

val LocalAppTypography = staticCompositionLocalOf<AppTypography> {
    error("No AppTypography provided - wrap your composable in KoobeTheme")
}

val LocalAppShapes = staticCompositionLocalOf<AppShapes> {
    error("No AppShapes provided - wrap your composable in KoobeTheme")
}