package com.jorgelobo.koobe.ui.components.composed.reports

import androidx.compose.foundation.layout.Arrangement
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
import com.jorgelobo.koobe.ui.theme.dimens.ListItemSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.ui.theme.dimens.TextSize

@Composable
fun ReportSummaryItem(
    modifier: Modifier = Modifier,
    config: ReportSummaryItemConfig
    ) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(ListItemSize.SecondaryHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.Tiny)
    ) {
        Text(
            text = config.label,
            style = typography.text.bodyMedium,
            color = colors.textColors.textPrimary,
            modifier = Modifier.width(TextSize.ReportSummaryLabel)
        )

        MoneyText(
            amount = config.amount,
            currencyType = config.currencyType,
            wholeFontSize = typography.numbers.bodyLarge.fontSize,
            decimalFontSize = typography.numbers.labelMedium.fontSize,
            textColor = colors.textColors.textPrimary,
            textAlign = TextAlign.Start,
            isEnabled = true,
            showNegativeSign = true
        )

        if (config.month != null) {
            Text(
                text = "(${config.month})",
                style = typography.numbers.bodyLarge,
                color = colors.textColors.textPrimary
            )
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewReportSummaryItem() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            ReportSummaryItem(
                config = ReportSummaryItemConfig(
                    label = stringResource(R.string.report_insight_worst_month),
                    month = "October",
                    amount = -240.25,
                    currencyType = CurrencyType.EUR
                )
            )

            ReportSummaryItem(
                config = ReportSummaryItemConfig(
                    label = stringResource(R.string.report_insight_highest_value),
                    amount = 636.55,
                    currencyType = CurrencyType.EUR
                )
            )
        }
    }
}