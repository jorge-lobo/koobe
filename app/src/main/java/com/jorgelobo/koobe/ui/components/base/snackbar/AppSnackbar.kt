package com.jorgelobo.koobe.ui.components.base.snackbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.Background
import com.jorgelobo.koobe.ui.components.model.BackgroundType
import com.jorgelobo.koobe.ui.components.model.IconName
import com.jorgelobo.koobe.ui.components.model.SnackBarConfig
import com.jorgelobo.koobe.ui.icons.getIconFromName
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import kotlinx.coroutines.launch

@Composable
fun AppSnackBar(
    config: SnackBarConfig,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors.snackBarColors
    val typography = AppTheme.typography.text

    Surface(
        modifier = modifier
            .clip(AppTheme.shapes.small),
        color = colors.snackBarContainer,
        tonalElevation = Spacing.Tiny,
        shadowElevation = Spacing.Micro
    ) {
        Row(
            modifier = Modifier
                .padding(Spacing.MediumSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(config.messageRes),
                color = colors.snackBarSupportingText,
                style = typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            config.actionLabelRes?.let { actionLabel ->
                TextButton(onClick = { config.onActionClick?.invoke() }) {
                    Text(
                        text = stringResource(actionLabel),
                        style = typography.bodyMedium,
                        color = colors.snackBarAction
                    )
                }
            }

            config.icon?.let { iconName ->
                IconButton(onClick = { config.onIconClick?.invoke() }) {
                    Icon(
                        imageVector = getIconFromName(iconName),
                        contentDescription = null,
                        tint = colors.snackBarIcon
                    )
                }
            }
        }
    }
}

@Composable
fun SnackBarDemoScreen() {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val snackBarMessage = stringResource(R.string.snackBar_message)
    val snackBarAction = stringResource(R.string.snackBar_action)
    val demoButtonLabel = stringResource(R.string.btn_save)

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                val config = SnackBarConfig(
                    messageRes = R.string.snackBar_message,
                    actionLabelRes = R.string.snackBar_action,
                    icon = IconName.EDIT,
                    onActionClick = { data.performAction() },
                    onIconClick = { scope.launch { data.dismiss() } }
                )
                AppSnackBar(
                    config = config,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = snackBarMessage,
                        actionLabel = snackBarAction,
                        duration = SnackbarDuration.Long
                    )
                }
            }) {
                Text(demoButtonLabel)
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewSnackBars() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            AppSnackBar(
                config = SnackBarConfig(
                    messageRes = R.string.snackBar_message,
                    actionLabelRes = R.string.snackBar_action,
                    icon = IconName.EDIT,
                    onActionClick = {},
                    onIconClick = {}
                )
            )

            SnackBarDemoScreen()
        }
    }
}