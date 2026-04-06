package com.jorgelobo.koobe.ui.components.base.buttons.base

import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.ui.components.model.enums.IconButtonType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

@Composable
fun IconButtonAppBar(
    onClick: () -> Unit,
    icon: IconPack,
    enabled: Boolean
) {
    IconButtonBase(
        onClick = onClick,
        type = IconButtonType.APP_BAR,
        isEnabled = enabled,
        icon = icon
    )
}

@Composable
fun IconButtonDisclosure(
    onClick: () -> Unit,
    icon: IconPack,
    enabled: Boolean
) {
    IconButtonBase(
        onClick = onClick,
        type = IconButtonType.DISCLOSURE,
        isEnabled = enabled,
        icon = icon
    )
}

@Composable
fun IconButtonInput(
    onClick: () -> Unit,
    icon: IconPack,
    enabled: Boolean
) {
    IconButtonBase(
        onClick = onClick,
        type = IconButtonType.INPUT,
        isEnabled = enabled,
        icon = icon
    )
}

@Composable
fun IconButtonListItem(
    onClick: () -> Unit,
    icon: IconPack,
    enabled: Boolean
) {
    IconButtonBase(
        onClick = onClick,
        type = IconButtonType.LIST_ITEM,
        isEnabled = enabled,
        icon = icon
    )
}

@Composable
fun IconButtonNavigation(
    onClick: () -> Unit,
    icon: IconPack,
    enabled: Boolean
) {
    IconButtonBase(
        onClick = onClick,
        type = IconButtonType.NAVIGATION,
        isEnabled = enabled,
        icon = icon
    )
}