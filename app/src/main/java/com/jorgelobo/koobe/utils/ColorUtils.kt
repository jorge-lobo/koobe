package com.jorgelobo.koobe.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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