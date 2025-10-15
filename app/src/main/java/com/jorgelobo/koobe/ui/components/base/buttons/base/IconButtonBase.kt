package com.jorgelobo.koobe.ui.components.base.buttons.base

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.ui.components.model.enums.IconButtonType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.IconSize

@Composable
fun IconButtonBase(
    onClick: () -> Unit,
    isEnabled: Boolean,
    iconUrl: ImageVector,
    type: IconButtonType
) {
    val iconColor = AppTheme.colors.iconColors

    val containerSize = when (type) {
        IconButtonType.APP_BAR -> IconSize.ExtraLarge
        IconButtonType.DISCLOSURE -> IconSize.Small
        else -> IconSize.Medium
    }

    val iconSize = when (type) {
        IconButtonType.DISCLOSURE -> IconSize.Small
        IconButtonType.LIST_ITEM -> IconSize.Small
        else -> IconSize.Medium
    }

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(containerSize),
        enabled = isEnabled
    ) {
        Icon(
            imageVector = iconUrl,
            modifier = Modifier
                .size(iconSize),
            contentDescription = null,
            tint = if (isEnabled) iconColor.iconPrimary else iconColor.iconDisabled
        )
    }
}