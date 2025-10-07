package com.jorgelobo.koobe.ui.components.base.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.TransactionType
import com.jorgelobo.koobe.domain.model.constants.UiState
import com.jorgelobo.koobe.ui.components.base.Background
import com.jorgelobo.koobe.ui.components.model.BackgroundType
import com.jorgelobo.koobe.ui.components.model.ButtonConfig
import com.jorgelobo.koobe.ui.components.model.ButtonType
import com.jorgelobo.koobe.ui.components.model.iconVector
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun AppButton(config: ButtonConfig, modifier: Modifier = Modifier) {
    val enabled = config.state == UiState.ENABLED

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

        ButtonType.ADD_TRANSACTION -> {
            val txType = config.transactionType
                ?: error("AddTransactionButton needs a TransactionType")
            AddTransactionButton(
                onClick = config.onClick,
                enabled = enabled,
                type = txType,
                modifier = modifier
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
                    state = UiState.ENABLED,
                    onClick = {}
                )
            )

            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_continue),
                    type = ButtonType.PRIMARY,
                    state = UiState.ENABLED,
                    onClick = {}
                )
            )

            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_create_shortcuts),
                    type = ButtonType.SECONDARY,
                    state = UiState.ENABLED,
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
                    icon = IconGeneral.CHANGE,
                    onClick = {}
                )
            )

            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_add_subcategory),
                    type = ButtonType.TEXT,
                    icon = IconGeneral.ADD,
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                AppButton(
                    ButtonConfig(
                        type = ButtonType.ADD_TRANSACTION,
                        state = UiState.ENABLED,
                        transactionType = TransactionType.INCOME,
                        onClick = {},
                        text = ""
                    ),
                    modifier = Modifier.weight(1f)
                )

                AppButton(
                    ButtonConfig(
                        type = ButtonType.ADD_TRANSACTION,
                        state = UiState.ENABLED,
                        transactionType = TransactionType.EXPENSE,
                        onClick = {},
                        text = ""
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}