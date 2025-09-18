package com.jorgelobo.koobe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LightColors = AppColorScheme(
    brandColor = BrandBlue,
    negative = AccentCoral,
    positive = AccentMint,
    neutralAccent = AccentGold,
    backgroundColors = BackgroundColors(
        splashBackground = BrandBlue,
        screenBackground = OffWhite
    ),
    containerColors = ContainerColors(
        containerPrimary = White,
        containerOutline = LightThemeGrey1,
        containerSelected = AccentMint,
        containerNeutralAmount = LightThemeGrey1,
        outlinePrimary = LightThemeGrey1,
        avatarContainerDefault = LightThemeGrey3,
        bottomSheetDragHandle = LightThemeGrey1,
        scrim = LightThemeGrey4,
        divider = LightThemeGrey1
    ),
    textColors = TextColors(
        textPrimary = LightThemeGrey4,
        textSecondary = BrandBlue,
        textHint = LightThemeGrey2,
        textLabel = BrandBlue,
        textSupportMessage = LightThemeGrey3,
        textActiveLabelText = BrandBlue,
        textUnselectedLabelText = LightThemeGrey3
    ),
    iconColors = IconColors(
        iconPrimary = BrandBlue,
        iconAvatar = White,
        iconSelected = BrandBlue,
        iconDisabled = LightThemeGrey2
    ),
    navigationColors = NavigationColors(
        navigationActiveIndicator = AccentMint,
        navigationActiveIcon = BrandBlue,
        navigationUnselectedIcon = LightThemeGrey3
    ),
    buttonColors = ButtonColors(
        buttonPrimaryContainer = BrandBlue,
        buttonPrimaryLabelText = White,
        buttonSecondaryContainer = White,
        buttonSecondaryOutline = BrandBlue,
        buttonSecondaryLabelText = BrandBlue,
        buttonSquareContainer = White,
        buttonSquareOutline = LightThemeGrey1,
        buttonSquareIcon = BrandBlue,
        buttonDisabledContainer = LightThemeGrey1,
        buttonDisabledOutline = LightThemeGrey1,
        buttonDisabledLabelText = LightThemeGrey2,
        buttonTextDefault = AccentBlue
    ),
    switchButtonColors = SwitchButtonColors(
        switchPrimary = BrandBlue,
        switchSecondary = White
    ),
    toggleButtonColors = ToggleButtonColors(
        toggleContainer = LightThemeGrey1,
        toggleSelectedContainer = BrandBlue,
        toggleUnselectedContainer = White,
        toggleSelectedLabelText = White,
        toggleUnselectedLabelText = BrandBlue
    ),
    radioButtonColors = RadioButtonColors(
        radioButtonSelectedIcon = BrandBlue,
        radioButtonUnselectedIcon = LightThemeGrey3
    ),
    tabColors = TabColors(
        tabActiveIndicator = BrandBlue
    ),
    keypadColors = KeypadColors(
        keypadContainer = LightThemeGrey1,
        keypadKeyPrimaryContainer = White,
        keypadKeySecondaryContainer = OffWhite,
        keypadKeyPressedContainer = AccentMint,
        keypadKeySymbol = BrandBlue
    ),
    snackBarColors = SnackBarColors(
        snackBarContainer = LightThemeGrey3,
        snackBarSupportingText = White,
        snackBarAction = AccentBlue,
        snackBarIcon = White
    ),
    progressBarColors = ProgressBarColors(
        progressBarTrack = OffWhite,
        progressBarOutline = LightThemeGrey1,
        progressBarActiveIndicator = BrandBlue,
        progressBarText = White
    ),
    listSelectorColors = ListSelectorColors(
        listSelectorContainer = LightThemeGrey1,
        listSelectorUnselected = White,
        listSelectorSelected = AccentMint,
        listSelectorText = BrandBlue
    )
)

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

val LocalAppColors = staticCompositionLocalOf<AppColorScheme> {
    error("No AppColorScheme provided - wrap your composable in KoobeTheme")
}

@Composable
fun KoobeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    CompositionLocalProvider(
        LocalAppColors provides colorScheme
    ) {
        content()
    }
}