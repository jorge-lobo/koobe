package com.jorgelobo.koobe.ui.components.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.color.AccentMint
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.getCurrencySymbol
import java.util.Locale
import kotlin.math.abs

@Composable
fun MoneyText(
    modifier: Modifier = Modifier,
    amount: Double,
    currencyType: CurrencyType,
    wholeFontSize: TextUnit,
    decimalFontSize: TextUnit,
    textColor: Color,
    textAlign: TextAlign,
    isEnabled: Boolean
) {
    val colors = AppTheme.colors
    val currencySymbol = getCurrencySymbol(currencyType)

    val absoluteAmount = abs(amount)
    val formatted = String.format(Locale.ENGLISH, "%.2f", absoluteAmount)
    val parts = formatted.split(".")
    val wholePart = parts.getOrNull(0) ?: "0"
    val decimalPart = parts.getOrNull(1) ?: "00"

    val textColor by animateColorAsState(
        targetValue = if (isEnabled) textColor else colors.textColors.textDisabled,
        label = "MoneyTextColor"
    )

    Text(
        buildAnnotatedString {
            // Whole number
            withStyle(
                style = SpanStyle(
                    fontSize = wholeFontSize,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            ) {
                append("$currencySymbol $wholePart")
            }

            // Decimal part
            withStyle(
                style = SpanStyle(
                    fontSize = decimalFontSize,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
            ) {
                append(".$decimalPart")
            }
        },
        modifier = modifier,
        textAlign = textAlign
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewMoneyText() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            val typography = AppTheme.typography

            MoneyText(
                modifier = Modifier.weight(1f),
                amount = 200.00,
                currencyType = CurrencyType.EUR,
                wholeFontSize = typography.numbers.titleMedium.fontSize,
                decimalFontSize = typography.numbers.bodyLarge.fontSize,
                textColor = AccentCoral,
                textAlign = TextAlign.End,
                isEnabled = true
            )

            MoneyText(
                modifier = Modifier.weight(1f),
                amount = 500.00,
                currencyType = CurrencyType.EUR,
                wholeFontSize = typography.numbers.titleMedium.fontSize,
                decimalFontSize = typography.numbers.bodyLarge.fontSize,
                textColor = AccentMint,
                textAlign = TextAlign.End,
                isEnabled = true
            )
        }
    }
}