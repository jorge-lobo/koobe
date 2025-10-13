package com.jorgelobo.koobe.ui.components.base.inputs

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.ui.components.base.Background
import com.jorgelobo.koobe.ui.components.base.text.MoneyText
import com.jorgelobo.koobe.ui.components.model.BackgroundType
import com.jorgelobo.koobe.ui.components.model.InputAmountConfig
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.ui.theme.dimens.ValueDisplaySize

@Composable
fun InputAmount(
    modifier: Modifier = Modifier,
    config: InputAmountConfig
) {
    val shape = AppTheme.shapes.medium
    val colors = AppTheme.colors
    val isEnabled = config.value > 0.0

    val iconColor by animateColorAsState(
        targetValue = if (isEnabled) colors.iconColors.iconPrimary
        else colors.iconColors.iconDisabled,
        label = "resetIconColor"
    )

    Box(
        modifier = modifier
            .height(ValueDisplaySize.Height)
            .width(ValueDisplaySize.Width)
            .clip(shape)
            .border(BorderDimens.Base, colors.containerColors.containerOutline, shape)
            .background(colors.containerColors.containerPrimary)
            .padding(horizontal = Spacing.Small),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))

            MoneyText(
                amount = config.value,
                currencySymbol = "€",
                config = config
            )

            Spacer(modifier = Modifier.width(Spacing.Small))

            IconButton(
                onClick = config.onResetClick,
                enabled = isEnabled,
                modifier = Modifier.size(IconSize.Large)
            ) {
                Icon(
                    imageVector = IconGeneral.RESET.icon,
                    contentDescription = "Reset amount",
                    tint = iconColor,
                    modifier = Modifier.size(IconSize.Medium)
                )
            }
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewInputAmount() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            InputAmount(
                config = InputAmountConfig(
                    value = 1.50,
                    currencySymbol = "€",
                    onResetClick = {},
                )
            )
        }
    }
}