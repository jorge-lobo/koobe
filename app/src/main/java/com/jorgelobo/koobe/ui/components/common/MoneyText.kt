package com.jorgelobo.koobe.ui.components.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.utils.getCurrencySymbol
import java.util.Locale

@Composable
fun MoneyText(
    amount: Double,
    currencyType: CurrencyType,
    wholeFontSize: TextUnit,
    decimalFontSize: TextUnit,
    textColor: Color,
    textAlign: TextAlign,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val currencySymbol = getCurrencySymbol(currencyType)

    val formatted = String.format(Locale.ENGLISH, "%.2f", amount)
    val parts = formatted.split(".")

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
                    color = textColor
                )
            ) {
                append("$currencySymbol ${parts[0]}")
            }

            // Decimal part
            withStyle(
                style = SpanStyle(
                    fontSize = decimalFontSize,
                    color = textColor
                )
            ) {
                append(".${parts[1]}")
            }
        },
        modifier = modifier,
        textAlign = textAlign
    )
}