package com.jorgelobo.koobe.ui.components.composed.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.ButtonSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun ConfirmCancelButtons(
    modifier: Modifier = Modifier,
    config: ConfirmCancelButtonsConfig
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(ButtonSize.ConfirmCancelButtons.ContainerHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        AppButton(
            ButtonConfig(
                text = config.cancelText,
                type = ButtonType.TEXT,
                onClick = config.onCancelClick
            )
        )

        Spacer(modifier = Modifier.width(Spacing.Large))

        AppButton(
            ButtonConfig(
                text = config.confirmText,
                type = ButtonType.TEXT,
                textColor = config.confirmTextColor,
                state = if (config.isConfirmEnabled) UiState.ENABLED else UiState.DISABLED,
                onClick = config.onConfirmClick
            )
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewActionButtons() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            ConfirmCancelButtons(
                config = ConfirmCancelButtonsConfig(
                    confirmText = stringResource(R.string.btn_apply),
                    cancelText = stringResource(R.string.btn_cancel),
                    isConfirmEnabled = false,
                    onConfirmClick = {},
                    onCancelClick = {}
                )
            )
        }
    }
}