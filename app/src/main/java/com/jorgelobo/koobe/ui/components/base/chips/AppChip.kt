package com.jorgelobo.koobe.ui.components.base.chips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.ChipSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun AppChip(
    modifier: Modifier = Modifier,
    text: String,
    onClick: (() -> Unit)? = null,
) {
    val chipModifier = modifier.height(ChipSize.ChipHeight)
    val shape = AppTheme.shapes.large
    val containerColor = AppTheme.colors.containerColors.containerSecondary
    val contentColor = AppTheme.colors.textColors.textSecondary

    if (onClick != null) {
        Surface(
            modifier = chipModifier,
            onClick = onClick,
            shape = shape,
            color = containerColor,
            contentColor = contentColor
        ) {
            AppChipContent(text)
        }
    } else {
        Surface(
            modifier = chipModifier,
            shape = shape,
            color = containerColor,
            contentColor = contentColor
        ) {
            AppChipContent(text)
        }
    }
}

@Composable
private fun AppChipContent(
    text: String
) {
    Row(
        modifier = Modifier.padding(horizontal = Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
    ) {
        Text(
            text = text,
            style = AppTheme.typography.text.labelMedium
        )
    }
}