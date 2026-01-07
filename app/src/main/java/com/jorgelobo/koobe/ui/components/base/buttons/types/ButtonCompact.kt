package com.jorgelobo.koobe.ui.components.base.buttons.types

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens
import com.jorgelobo.koobe.ui.theme.dimens.ButtonSize

@Composable
fun ButtonCompact(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    text: String
) {
    val colors = AppTheme.colors.buttonColors
    val shape = AppTheme.shapes.medium

    Surface(
        modifier = modifier
            .width(ButtonSize.CompactButton.Width)
            .height(ButtonSize.CompactButton.Height),
        shape = shape,
        color = colors.buttonSecondaryContainer,
        border = BorderStroke(
            BorderDimens.Base,
            colors.buttonSecondaryOutline
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = AppTheme.typography.text.bodyLarge,
                color = colors.buttonSecondaryLabelText
            )
        }
    }
}