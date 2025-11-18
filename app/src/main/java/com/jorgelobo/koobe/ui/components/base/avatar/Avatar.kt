package com.jorgelobo.koobe.ui.components.base.avatar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentBlue
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    type: AvatarType,
    icon: ImageVector,
    color: Color,
    isSelected: Boolean = false
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

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewAvatar() {
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
                icon = IconPack.TRANSPORTATION.icon,
                color = AccentBlue,
                isSelected = false
            )
        }
    }
}