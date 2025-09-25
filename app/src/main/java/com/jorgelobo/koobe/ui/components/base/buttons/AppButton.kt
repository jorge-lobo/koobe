package com.jorgelobo.koobe.ui.components.base.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.Background
import com.jorgelobo.koobe.ui.components.model.BackgroundType
import com.jorgelobo.koobe.ui.components.model.ButtonConfig
import com.jorgelobo.koobe.ui.components.model.ButtonState
import com.jorgelobo.koobe.ui.components.model.ButtonType
import com.jorgelobo.koobe.ui.components.model.IconName
import com.jorgelobo.koobe.ui.components.model.iconVector
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun AppButton(config: ButtonConfig, modifier: Modifier = Modifier) {
    val enabled = config.state == ButtonState.ENABLED

    when (config.type) {
        ButtonType.PRIMARY -> PrimaryButton(
            onClick = config.onClick,
            enabled = enabled,
            text = config.text
        )

        ButtonType.SECONDARY -> SecondaryButton(
            onClick = config.onClick,
            enabled = enabled,
            text = config.text
        )

        ButtonType.SECONDARY_COMPACT -> CompactButton(
            onClick = config.onClick,
            enabled = enabled,
            text = config.text,
            modifier = modifier
        )

        ButtonType.SQUARE -> {
            val iconVector = config.iconVector()
                ?: error("SquareButton needs an icon")
            SquareButton(
                onClick = config.onClick,
                enabled = enabled,
                iconUrl = iconVector
            )
        }

        ButtonType.TEXT -> {
            val iconVector = config.iconVector()
            TextButton(
                onClick = config.onClick,
                enabled = enabled,
                text = config.text,
                iconUrl = iconVector
            )
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewButtons() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_continue),
                    type = ButtonType.PRIMARY,
                    state = ButtonState.DISABLED,
                    onClick = {}
                )
            )

            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_continue),
                    type = ButtonType.PRIMARY,
                    state = ButtonState.ENABLED,
                    onClick = {}
                )
            )

            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_create_shortcuts),
                    type = ButtonType.SECONDARY,
                    state = ButtonState.ENABLED,
                    onClick = {}
                )
            )

            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_today),
                    type = ButtonType.SECONDARY_COMPACT,
                    onClick = {}
                )
            )

            AppButton(
                ButtonConfig(
                    text = "",
                    type = ButtonType.SQUARE,
                    icon = IconName.CHANGE,
                    onClick = {}
                )
            )

            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_add_subcategory),
                    type = ButtonType.TEXT,
                    icon = IconName.ADD,
                    onClick = {}
                )
            )

            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_view_all),
                    type = ButtonType.TEXT,
                    onClick = {}
                )
            )
        }
    }
}