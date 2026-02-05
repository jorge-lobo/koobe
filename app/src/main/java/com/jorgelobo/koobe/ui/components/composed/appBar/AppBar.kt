package com.jorgelobo.koobe.ui.components.composed.appBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.buttons.base.IconButtonAppBar
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.mappers.getIconFromName
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.AppBarSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonAppBar(
    modifier: Modifier = Modifier,
    config: AppBarConfig
) {
    val colors = AppTheme.colors

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.containerColors.containerPrimary)
            .height(AppBarSize.Height)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Leading
            Box(
                modifier = Modifier.size(AppBarSize.Height),
                contentAlignment = Alignment.Center
            ) {
                IconButtonAppBar(
                    onClick = config.leadingAction.onClick,
                    iconUrl = getIconFromName(config.leadingAction.icon),
                    enabled = true
                )
            }

            // Headline
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = config.headline,
                    style = AppTheme.typography.text.titleLarge,
                    color = colors.textColors.textSecondary
                )
            }

            // Trailing
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                config.trailingActions.forEach { action ->
                    IconButtonAppBar(
                        onClick = action.onClick,
                        iconUrl = getIconFromName(action.icon),
                        enabled = true
                    )
                }
            }
        }

    }
}

@Composable
fun LogoAppBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppBarSize.Height)
            .background(color = AppTheme.colors.containerColors.containerPrimary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Spacing.MediumSmall),
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.cd_logo),
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewAppBars() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            CommonAppBar(
                config = AppBarConfig(
                    headline = stringResource(R.string.headline_shortcut_editor),
                    leadingAction = AppBarAction(IconGeneral.BACK) {},
                    trailingActions = listOf(
                        AppBarAction(IconGeneral.CHANGE) {},
                        AppBarAction(IconGeneral.DELETE) {}
                    )
                )
            )

            Spacer(modifier = Modifier.height(Spacing.Medium))

            LogoAppBar()
        }
    }
}