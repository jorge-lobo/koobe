package com.jorgelobo.koobe.ui.components.composed.emptyState

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.EmptyStateIconType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.color.LightThemeGrey2

@Composable
fun EmptyStateContent(
    modifier: Modifier = Modifier,
    config: EmptyStateContentConfig
) {
    val typography = AppTheme.typography.text
    val colors = AppTheme.colors.textColors

    val iconSize = when (config.iconType) {
        EmptyStateIconType.CARD -> IconSize.ExtraLarge
        EmptyStateIconType.BACKGROUND -> IconSize.Giant
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = config.icon,
            contentDescription = null,
            tint = config.iconTint,
            modifier = Modifier.size(iconSize)
        )

        Text(
            text = config.message,
            style = typography.titleMedium,
            color = colors.textSupportMessage
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewEmptyState() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            EmptyStateContent(
                config = EmptyStateContentConfig(
                    message = stringResource(R.string.empty_dashboard_shortcuts),
                    icon = IconGeneral.EMPTY.icon,
                    iconTint = AccentCoral,
                    iconType = EmptyStateIconType.BACKGROUND
                )
            )

            EmptyStateContent(
                config = EmptyStateContentConfig(
                    message = stringResource(R.string.empty_dashboard_shortcuts),
                    icon = IconGeneral.EMPTY.icon,
                    iconTint = LightThemeGrey2,
                    iconType = EmptyStateIconType.CARD
                )
            )
        }
    }
}