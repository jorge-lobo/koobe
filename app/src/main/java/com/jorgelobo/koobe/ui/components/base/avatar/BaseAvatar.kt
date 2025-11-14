package com.jorgelobo.koobe.ui.components.base.avatar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.AvatarSize
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.R

@Composable
fun BaseAvatar(
    modifier: Modifier = Modifier,
    config: AvatarConfig
) {
    val isSelected = config.isSelected
    val shapes = AppTheme.shapes
    val iconColors = AppTheme.colors.iconColors
    val selectedContainer = AppTheme.colors.containerColors.containerSelected

    val (containerSize, shape, iconSize) = when (config.type) {
        AvatarType.SMALL -> Triple(AvatarSize.Small, shapes.extraSmall, IconSize.Micro)
        AvatarType.MEDIUM -> Triple(AvatarSize.Medium, shapes.small, IconSize.ExtraSmall)
        AvatarType.LARGE -> Triple(AvatarSize.Large, shapes.medium, IconSize.Large)
        AvatarType.EXTRA_LARGE -> Triple(AvatarSize.ExtraLarge, shapes.large, IconSize.ExtraLarge)
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) selectedContainer else config.color,
        label = "avatarBackground"
    )

    val iconTint by animateColorAsState(
        targetValue = if (isSelected) iconColors.iconSelected else iconColors.iconAvatar,
        label = "avatarIconTint"
    )

    Box(
        modifier = modifier
            .size(containerSize)
            .clip(shape)
            .background(backgroundColor, shape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = config.icon,
            contentDescription = stringResource(R.string.cd_avatar),
            tint = iconTint,
            modifier = Modifier.size(iconSize)
        )
    }
}