package com.jorgelobo.koobe.ui.components.base.buttons.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.ui.components.model.enums.IconButtonType

@Composable
fun IconButtonAppBar(
    onClick: () -> Unit,
    iconUrl: ImageVector,
    enabled: Boolean
) {
    IconButtonBase(
        onClick = onClick,
        type = IconButtonType.APP_BAR,
        isEnabled = enabled,
        iconUrl = iconUrl
    )
}

@Composable
fun IconButtonDisclosure(
    onClick: () -> Unit,
    iconUrl: ImageVector,
    enabled: Boolean
) {
    IconButtonBase(
        onClick = onClick,
        type = IconButtonType.DISCLOSURE,
        isEnabled = enabled,
        iconUrl = iconUrl
    )
}

@Composable
fun IconButtonInput(
    onClick: () -> Unit,
    iconUrl: ImageVector,
    enabled: Boolean
) {
    IconButtonBase(
        onClick = onClick,
        type = IconButtonType.INPUT,
        isEnabled = enabled,
        iconUrl = iconUrl
    )
}

@Composable
fun IconButtonListItem(
    onClick: () -> Unit,
    iconUrl: ImageVector,
    enabled: Boolean
) {
    IconButtonBase(
        onClick = onClick,
        type = IconButtonType.LIST_ITEM,
        isEnabled = enabled,
        iconUrl = iconUrl
    )
}

@Composable
fun IconButtonNavigation(
    onClick: () -> Unit,
    iconUrl: ImageVector,
    enabled: Boolean
) {
    IconButtonBase(
        onClick = onClick,
        type = IconButtonType.NAVIGATION,
        isEnabled = enabled,
        iconUrl = iconUrl
    )
}