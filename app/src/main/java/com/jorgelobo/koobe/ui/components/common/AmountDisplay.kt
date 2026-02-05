package com.jorgelobo.koobe.ui.components.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.color.AccentMint
import com.jorgelobo.koobe.ui.theme.dimens.AmountDisplaySize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun AmountDisplay(
    modifier: Modifier = Modifier,
    amount: Double,
    currencyType: CurrencyType
) {
    val colors = AppTheme.colors
    val shape = AppTheme.shapes.small
    val typography = AppTheme.typography.numbers

    val backgroundColor by animateColorAsState(
        targetValue = when {
            amount > 0.0 -> AccentMint
            amount < 0.0 -> AccentCoral
            else -> colors.containerColors.containerNeutralAmount
        },
        label = "AmountDisplayColor"
    )

    Box(
        modifier = modifier
            .width(AmountDisplaySize.Width)
            .height(AmountDisplaySize.Height)
            .background(backgroundColor, shape)
            .padding(Spacing.Tiny),
        contentAlignment = Alignment.CenterEnd
    ) {
        MoneyText(
            amount = amount,
            currencyType = currencyType,
            wholeFontSize = typography.bodyLarge.fontSize,
            decimalFontSize = typography.labelMedium.fontSize,
            textColor = colors.textColors.textDisplay,
            textAlign = TextAlign.End,
            isEnabled = true
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewAmountDisplay() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            AmountDisplay(
                amount = 90.0,
                currencyType = CurrencyType.EUR
            )

            AmountDisplay(
                amount = -50.50,
                currencyType = CurrencyType.EUR
            )

            AmountDisplay(
                amount = 0.0,
                currencyType = CurrencyType.EUR
            )
        }
    }
}