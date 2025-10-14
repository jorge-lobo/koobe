package com.jorgelobo.koobe.ui.components.base.avatars

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.ui.components.base.Background
import com.jorgelobo.koobe.ui.components.model.AvatarType
import com.jorgelobo.koobe.ui.components.model.AvatarConfig
import com.jorgelobo.koobe.ui.components.model.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconCategory
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentBlue
import com.jorgelobo.koobe.ui.theme.color.AccentGold
import com.jorgelobo.koobe.ui.theme.dimens.AvatarSize
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

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
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun Avatar(
    type: AvatarType,
    icon: ImageVector,
    color: Color,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    BaseAvatar(
        modifier = modifier,
        config = AvatarConfig(
            type = type,
            icon = icon,
            color = color,
            isSelected = isSelected
        )
    )
}

@Composable
fun SelectableAvatar(
    type: AvatarType,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onSelected: (Boolean) -> Unit = {}
) {
    var isSelected by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "avatarScale"
    )

    Box(
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clickable {
                isSelected = !isSelected
                onSelected(isSelected)
            }
    ) {
        Avatar(
            type = type,
            icon = icon,
            color = color,
            isSelected = isSelected
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewAvatars() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            Avatar(
                type = AvatarType.MEDIUM,
                icon = IconCategory.TRANSPORTATION.icon,
                color = AccentBlue,
                isSelected = false
            )

            SelectableAvatar(
                type = AvatarType.LARGE,
                icon = IconCategory.APPAREL.icon,
                color = AccentGold,
                onSelected = { }
            )
        }
    }
}