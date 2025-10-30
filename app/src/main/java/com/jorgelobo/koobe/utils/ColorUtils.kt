package com.jorgelobo.koobe.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.payment.PaymentMethod
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.color.AccentMint

@Composable
fun getValueColor(
    value: Double,
    neutralColor: Color = AppTheme.colors.containerColors.containerNeutralAmount
): Color = when {
    value > 0.0 -> AccentMint
    value < 0.0 -> AccentCoral
    else -> neutralColor
}

@Composable
fun resolveColor(colorString: String?): Color {
    if (colorString.isNullOrBlank()) return AppTheme.colors.containerColors.containerNeutralAmount

    return try {
        Color(colorString.toColorInt())
    } catch (_: IllegalArgumentException) {
        AppTheme.colors.containerColors.containerNeutralAmount
    }
}

@Composable
fun Category.resolveColor(): Color {
    return resolveColor(color)
}

@Composable
fun PaymentMethod.resolveColor(): Color {
    return resolveColor(color)
}