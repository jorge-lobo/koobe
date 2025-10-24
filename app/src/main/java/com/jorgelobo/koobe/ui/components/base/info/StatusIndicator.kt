package com.jorgelobo.koobe.ui.components.base.info

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.color.AccentGold
import com.jorgelobo.koobe.ui.theme.color.AccentMint
import com.jorgelobo.koobe.ui.theme.dimens.Height
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun StatusIndicator(
    modifier: Modifier = Modifier,
    percentage: Double
) {
    val shape = AppTheme.shapes.extraSmall
    val size = Height.StatusIndicator

    val backgroundColor by animateColorAsState(
        targetValue = when {
            percentage >= 1.0 -> AccentCoral
            percentage < 0.8 -> AccentMint
            else -> AccentGold
        },
        label = "StatusIndicatorColor"
    )

    val icon = if (percentage < 0.8) IconGeneral.CHECK.icon else IconGeneral.WARNING.icon

    val iconColor = AppTheme.colors.iconColors.iconAvatar

    Box(
        modifier = modifier
            .size(size)
            .background(backgroundColor, shape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewWarningIndicator() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            StatusIndicator(
                percentage = 0.5
            )

            StatusIndicator(
                percentage = 1.2
            )
        }
    }
}