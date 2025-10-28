package com.jorgelobo.koobe.ui.components.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.color.AccentMint
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun AppBadge(
    value: Int,
    isExpanded: Boolean
) {
    val colors = AppTheme.colors

    val containerColor by animateColorAsState(
        targetValue = if (isExpanded) AccentMint else AccentCoral,
        label = "BadgeContainerColor"
    )

    Badge(
        containerColor = containerColor,
        contentColor = colors.textColors.textDisplay,
        content = {
            Text(
                text = value.toString()
            )
        }
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewBadge() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            AppBadge(
                value = 2,
                isExpanded = false
            )

            AppBadge(
                value = 4,
                isExpanded = true
            )
        }
    }
}