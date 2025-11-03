package com.jorgelobo.koobe.ui.components.composed.appBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.buttons.base.IconButtonAppBar
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.icons.getIconFromName
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
    TopAppBar(
        modifier = modifier.height(AppBarSize.Height),
        title = {
            Text(
                text = config.headline,
                style = AppTheme.typography.text.titleLarge
            )
        },
        navigationIcon = {
            IconButtonAppBar(
                onClick = config.leadingAction.onClick,
                iconUrl = getIconFromName(config.leadingAction.icon),
                enabled = true
            )
        },
        actions = {
            config.trailingActions.forEach { action ->
                IconButtonAppBar(
                    onClick = action.onClick,
                    iconUrl = getIconFromName(action.icon),
                    enabled = true
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.containerColors.containerPrimary,
            titleContentColor = colors.textColors.textSecondary
        )
    )
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
    KoobeTheme {
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