package com.jorgelobo.koobe.ui.components.composed.avatar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconCategory
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentGold
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

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
fun PreviewASelectableAvatar() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            SelectableAvatar(
                type = AvatarType.LARGE,
                icon = IconCategory.APPAREL.icon,
                color = AccentGold,
                onSelected = { }
            )
        }
    }
}