package com.jorgelobo.koobe.ui.theme

// TODO assign correct colors for dark theme
val DarkColors = LightColors.copy(
    backgroundColors = LightColors.backgroundColors.copy(
        screenBackground = DarkThemeGrey1
    ),
    containerColors = LightColors.containerColors.copy(
        containerPrimary = DarkThemeGrey3,
        containerOutline = BrandBlue
    ),
    textColors = LightColors.textColors.copy(
        textPrimary = White,
        textSecondary = DarkThemeGrey4
    )
)