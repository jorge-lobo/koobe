package com.jorgelobo.koobe.ui.theme.color

import androidx.compose.ui.graphics.Color

data class AppColorScheme(
    val brandColor: Color,
    val negative: Color,
    val positive: Color,
    val neutralAccent: Color,

    val backgroundColors: BackgroundColors,
    val containerColors: ContainerColors,
    val textColors: TextColors,
    val iconColors: IconColors,
    val navigationColors: NavigationColors,
    val buttonColors: ButtonColors,
    val switchButtonColors: SwitchButtonColors,
    val toggleButtonColors: ToggleButtonColors,
    val radioButtonColors: RadioButtonColors,
    val tabColors: TabColors,
    val keypadColors: KeypadColors,
    val snackBarColors: SnackBarColors,
    val progressBarColors: ProgressBarColors,
    val listSelectorColors: ListSelectorColors
)

data class BackgroundColors(
    val splashBackground: Color,
    val screenBackground: Color
)

data class ContainerColors(
    val containerPrimary: Color,
    val containerOutline: Color,
    val containerSelected: Color,
    val containerNeutralAmount: Color,
    val outlinePrimary: Color,
    val avatarContainerDefault: Color,
    val bottomSheetDragHandle: Color,
    val scrim: Color,
    val divider: Color
)

data class TextColors(
    val textPrimary: Color,
    val textSecondary: Color,
    val textHint: Color,
    val textLabel: Color,
    val textSupportMessage: Color,
    val textActiveLabelText: Color,
    val textUnselectedLabelText: Color
)

data class IconColors(
    val iconPrimary: Color,
    val iconAvatar: Color,
    val iconSelected: Color,
    val iconDisabled: Color,
    val iconWarning: Color,
    val iconTextButton: Color
)

data class NavigationColors(
    val navigationActiveIndicator: Color,
    val navigationActiveIcon: Color,
    val navigationUnselectedIcon: Color
)

data class ButtonColors(
    val buttonPrimaryContainer: Color,
    val buttonPrimaryLabelText: Color,
    val buttonSecondaryContainer: Color,
    val buttonSecondaryOutline: Color,
    val buttonSecondaryLabelText: Color,
    val buttonSquareContainer: Color,
    val buttonSquareOutline: Color,
    val buttonDisabledContainer: Color,
    val buttonDisabledOutline: Color,
    val buttonDisabledLabelText: Color,
    val buttonSquareIcon: Color,
    val buttonTextDefault: Color
)

data class SwitchButtonColors(
    val switchPrimary: Color,
    val switchSecondary: Color
)

data class ToggleButtonColors(
    val toggleContainer: Color,
    val toggleSelectedContainer: Color,
    val toggleSelectedLabelText: Color,
    val toggleUnselectedContainer: Color,
    val toggleUnselectedLabelText: Color
)

data class RadioButtonColors(
    val radioButtonSelectedIcon: Color,
    val radioButtonUnselectedIcon: Color,
)

data class TabColors(
    val tabActiveIndicator: Color
)

data class KeypadColors(
    val keypadContainer: Color,
    val keypadKeyPrimaryContainer: Color,
    val keypadKeySecondaryContainer: Color,
    val keypadKeyPressedContainer: Color,
    val keypadKeySymbol: Color
)

data class SnackBarColors(
    val snackBarContainer: Color,
    val snackBarSupportingText: Color,
    val snackBarAction: Color,
    val snackBarIcon: Color
)

data class ProgressBarColors(
    val progressBarTrack: Color,
    val progressBarOutline: Color,
    val progressBarActiveIndicator: Color,
    val progressBarText: Color
)

data class ListSelectorColors(
    val listSelectorContainer: Color,
    val listSelectorUnselected: Color,
    val listSelectorSelected: Color,
    val listSelectorText: Color
)