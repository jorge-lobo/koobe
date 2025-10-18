package com.jorgelobo.koobe.ui.components.composed.containers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.MetricType
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.common.MoneyText
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.ScreenType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.color.AccentMint
import com.jorgelobo.koobe.utils.getValueColor
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens
import com.jorgelobo.koobe.ui.theme.dimens.CardSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun MetricDisplay(
    modifier: Modifier = Modifier,
    screenType: ScreenType,
    currencyType: CurrencyType,
    metricType: MetricType,
    value: Double
) {
    val isBalance = metricType == MetricType.BALANCE

    val colors = AppTheme.colors
    val typography = AppTheme.typography.text
    val shapes = AppTheme.shapes
    val width = CardSize.Balance.Width
    val height = CardSize.Balance.Height
    val displayWidth = if (isBalance) width.AmountDisplayLarge else width.AmountDisplaySmall

    val parentBackground = colors.containerColors.containerPrimary
    val parentBorder = colors.containerColors.containerOutline
    val parentShape = shapes.medium

    val metricBackground = when (metricType) {
        MetricType.BALANCE -> if (screenType == ScreenType.REPORTS) colors.containerColors.containerPrimary else getValueColor(
            value
        )

        MetricType.INCOME -> AccentMint
        MetricType.EXPENSE -> AccentCoral
    }
    val metricShape = shapes.small

    val label = when (metricType) {
        MetricType.BALANCE -> if (screenType in listOf(
                ScreenType.DASHBOARD,
                ScreenType.REPORTS
            )
        ) stringResource(R.string.card_finance_stats_balance_overall) else stringResource(R.string.card_finance_stats_balance)

        MetricType.INCOME -> stringResource(R.string.card_finance_stats_income)
        MetricType.EXPENSE -> stringResource(R.string.card_finance_stats_expenses)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height.Container)
            .clip(parentShape)
            .background(parentBackground, parentShape)
            .border(BorderDimens.Base, parentBorder)
            .padding(
                start = Spacing.Small,
                end = Spacing.Tiny,
                top = Spacing.Tiny,
                bottom = Spacing.Tiny
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        Text(
            text = label,
            style = if (isBalance) typography.titleMedium else typography.bodySmall,
            color = colors.textColors.textSecondary,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .width(displayWidth)
                .height(height.AmountDisplay)
                .clip(metricShape)
                .background(metricBackground, metricShape)
                .padding(horizontal = Spacing.Tiny)
                .align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center
        ) {
            MoneyText(
                amount = value,
                currencyType = currencyType,
                wholeFontSize = if (isBalance) typography.titleMedium.fontSize else typography.bodyLarge.fontSize,
                decimalFontSize = if (isBalance) typography.bodyLarge.fontSize else typography.labelMedium.fontSize,
                textColor = colors.textColors.textDisplay,
                textAlign = TextAlign.End,
                isEnabled = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun BalanceDisplay(
    modifier: Modifier = Modifier,
    screenType: ScreenType,
    currencyType: CurrencyType,
    value: Double
) {
    MetricDisplay(
        modifier = modifier,
        screenType = screenType,
        currencyType = currencyType,
        metricType = MetricType.BALANCE,
        value = value
    )
}

@Composable
fun SubMetricDisplay(
    modifier: Modifier = Modifier,
    screenType: ScreenType,
    currencyType: CurrencyType,
    metricType: MetricType,
    value: Double
) {
    MetricDisplay(
        modifier = modifier,
        screenType = screenType,
        currencyType = currencyType,
        metricType = metricType,
        value = value
    )
}

@Composable
fun BalanceContainer(
    modifier: Modifier = Modifier,
    config: BalanceContainerConfig
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        BalanceDisplay(
            screenType = config.screenType,
            currencyType = config.currencyType,
            value = config.balance
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            SubMetricDisplay(
                modifier = Modifier.weight(1f),
                screenType = config.screenType,
                currencyType = config.currencyType,
                metricType = MetricType.INCOME,
                value = config.income
            )

            SubMetricDisplay(
                modifier = Modifier.weight(1f),
                screenType = config.screenType,
                currencyType = config.currencyType,
                metricType = MetricType.EXPENSE,
                value = config.expenses
            )
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewBalanceContainer() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            BalanceContainer(
                config = BalanceContainerConfig(
                    balance = 90.0,
                    income = 360.0,
                    expenses = 270.0,
                    currencyType = CurrencyType.GBP,
                    screenType = ScreenType.HISTORIC
                )
            )
        }
    }
}