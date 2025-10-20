package com.jorgelobo.koobe.ui.components.composed.containers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.common.MoneyText
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.color.AccentMint
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens
import com.jorgelobo.koobe.ui.theme.dimens.CardSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
private fun StatValue(
    amount: Double,
    currencyType: CurrencyType,
    color: Color,
    modifier: Modifier = Modifier
) {
    val typography = AppTheme.typography.numbers

    MoneyText(
        amount = amount,
        currencyType = currencyType,
        wholeFontSize = typography.titleMedium.fontSize,
        decimalFontSize = typography.bodyLarge.fontSize,
        textColor = color,
        textAlign = TextAlign.End,
        isEnabled = true,
        modifier = modifier
    )
}

@Composable
fun QuickStatItem(
    modifier: Modifier = Modifier,
    label: String,
    income: Double,
    expense: Double,
    currencyType: CurrencyType
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(CardSize.QuickStats.RowHeight)
            .padding(horizontal = Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = typography.text.bodyLarge,
            color = colors.textColors.textSecondary,
            modifier = Modifier.weight(1f)
        )

        StatValue(
            amount = income,
            currencyType = currencyType,
            color = AccentMint,
            modifier = Modifier.weight(1f)
        )

        StatValue(
            amount = expense,
            currencyType = currencyType,
            color = AccentCoral,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun QuickStatsCard(
    modifier: Modifier = Modifier,
    config: QuickStatsCardConfig
) {
    val colors = AppTheme.colors.containerColors
    val shape = AppTheme.shapes.medium

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(CardSize.QuickStats.CardHeight)
            .clip(shape)
            .background(colors.containerPrimary, shape)
            .border(BorderDimens.Base, colors.containerOutline, shape),
        verticalArrangement = Arrangement.Center
    ) {
        // Daily stats
        QuickStatItem(
            label = stringResource(R.string.card_quick_stats_day),
            income = config.dailyIncome,
            expense = config.dailyExpenses,
            currencyType = config.currencyType
        )

        // Weekly stats
        QuickStatItem(
            label = stringResource(R.string.card_quick_stats_week),
            income = config.weeklyIncome,
            expense = config.weeklyExpenses,
            currencyType = config.currencyType
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewQuickStatsContainer() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            QuickStatsCard(
                config = QuickStatsCardConfig(
                    dailyIncome = 50.00,
                    dailyExpenses = 20.00,
                    weeklyIncome = 400.00,
                    weeklyExpenses = 200.00,
                    currencyType = CurrencyType.EUR
                )
            )
        }
    }
}