package com.jorgelobo.koobe.ui.components.base.buttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.ui.components.model.IconButtonType

@Composable
fun AppBarIconButton(
    onClick: () -> Unit,
    iconUrl: ImageVector,
    enabled: Boolean
) {
    BaseIconButton(
        onClick = onClick,
        type = IconButtonType.APP_BAR,
        isEnabled = enabled,
        iconUrl = iconUrl
    )
}

@Composable
fun DisclosureIconButton(
    onClick: () -> Unit,
    iconUrl: ImageVector,
    enabled: Boolean
) {
    BaseIconButton(
        onClick = onClick,
        type = IconButtonType.DISCLOSURE,
        isEnabled = enabled,
        iconUrl = iconUrl
    )
}

@Composable
fun InputIconButton(
    onClick: () -> Unit,
    iconUrl: ImageVector,
    enabled: Boolean
) {
    BaseIconButton(
        onClick = onClick,
        type = IconButtonType.INPUT,
        isEnabled = enabled,
        iconUrl = iconUrl
    )
}

@Composable
fun ListItemIconButton(
    onClick: () -> Unit,
    iconUrl: ImageVector,
    enabled: Boolean
) {
    BaseIconButton(
        onClick = onClick,
        type = IconButtonType.LIST_ITEM,
        isEnabled = enabled,
        iconUrl = iconUrl
    )
}