package com.jorgelobo.koobe.ui.components.base.text

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.jorgelobo.koobe.ui.components.model.InputAmountConfig
import com.jorgelobo.koobe.ui.theme.AppTheme

@Composable
fun MoneyText(
    amount: Double,
    currencySymbol: String,
    config: InputAmountConfig,
    modifier: Modifier = Modifier
) {
    val typography = AppTheme.typography.numbers
    val colors = AppTheme.colors

    val formatted = String.format(java.util.Locale.ENGLISH, "%.2f", amount)
    val parts = formatted.split(".")

    val isEnabled = config.value > 0.0

    val textColor by animateColorAsState(
        targetValue = if (isEnabled) colors.textColors.textPrimary
        else colors.textColors.textDisabled
    )

    Text(
        buildAnnotatedString {
            // Whole number
            withStyle(
                style = SpanStyle(
                    fontSize = typography.titleMedium.fontSize,
                    fontWeight = typography.titleMedium.fontWeight,
                    color = textColor
                )
            ) {
                append("$currencySymbol ${parts[0]}")
            }

            // Decimal part
            withStyle(
                style = SpanStyle(
                    fontSize = typography.labelMedium.fontSize,
                    fontWeight = typography.labelMedium.fontWeight,
                    color = textColor
                )
            ) {
                append(".${parts[1]}")
            }
        },
        modifier = modifier
    )
}