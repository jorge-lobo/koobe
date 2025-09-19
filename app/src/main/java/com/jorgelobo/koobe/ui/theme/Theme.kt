package com.jorgelobo.koobe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.jorgelobo.koobe.ui.theme.color.DarkColors
import com.jorgelobo.koobe.ui.theme.color.LightColors
import com.jorgelobo.koobe.ui.theme.locals.LocalAppColors
import com.jorgelobo.koobe.ui.theme.locals.LocalAppShapes
import com.jorgelobo.koobe.ui.theme.locals.LocalAppTypography
import com.jorgelobo.koobe.ui.theme.shapes.AppShapes
import com.jorgelobo.koobe.ui.theme.typography.AppTypography
import com.jorgelobo.koobe.ui.theme.typography.NumberTypography
import com.jorgelobo.koobe.ui.theme.typography.TextTypography

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