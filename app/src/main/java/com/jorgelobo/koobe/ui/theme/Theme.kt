package com.jorgelobo.koobe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun KoobeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val typography = AppTypography(
        text = TextTypography,
        numbers = NumberTypography
    )
    val shapes = AppShapes()

    CompositionLocalProvider(
        LocalAppColors provides colorScheme,
        LocalAppTypography provides typography,
        LocalAppShapes provides shapes
    ) {
        content()
    }
}