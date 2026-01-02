package com.jorgelobo.koobe.ui.screen.categories.selector.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.base.buttons.types.ButtonText
import com.jorgelobo.koobe.ui.components.composed.emptyState.EmptyStateContent
import com.jorgelobo.koobe.ui.components.composed.emptyState.EmptyStateContentConfig
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.EmptyStateIconType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.color.LightThemeGrey2
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun CategoryDetailContent(
    isEmpty: Boolean,
    isContinueEnabled: Boolean,
    emptyHeadlineRes: Int,
    emptyHintRes: Int,
    createButtonRes: Int,
    onCreateClick: () -> Unit,
    onContinueClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val textColor = AppTheme.colors.textColors
    val typography = AppTheme.typography.text

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isEmpty) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(Spacing.Giant))

                EmptyStateContent(
                    config = EmptyStateContentConfig(
                        message = stringResource(emptyHeadlineRes),
                        icon = IconGeneral.EMPTY.icon,
                        iconTint = LightThemeGrey2,
                        iconType = EmptyStateIconType.BACKGROUND
                    )
                )
            }
        } else {
            content()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                ButtonText(
                    onClick = onCreateClick,
                    enabled = true,
                    text = stringResource(createButtonRes),
                    textColor = AppTheme.colors.buttonColors.buttonTextDefault,
                    iconUrl = IconGeneral.ADD.icon
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (isEmpty) {
            Text(
                text = stringResource(emptyHintRes),
                style = typography.bodySmall,
                color = textColor.textSupportMessage,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.Medium))

            AppButton(
                ButtonConfig(
                    text = stringResource(createButtonRes),
                    type = ButtonType.SECONDARY,
                    state = UiState.ENABLED,
                    onClick = onCreateClick
                )
            )
        }

        Spacer(modifier = Modifier.height(Spacing.Medium))

        AppButton(
            ButtonConfig(
                text = stringResource(R.string.btn_continue),
                type = ButtonType.PRIMARY,
                state = if (isContinueEnabled) UiState.ENABLED else UiState.DISABLED,
                onClick = onContinueClick
            )
        )
    }
}