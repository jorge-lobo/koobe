package com.jorgelobo.koobe.ui.components.base.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jorgelobo.koobe.ui.components.model.ButtonType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.ButtonSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BaseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    backgroundColor: Color,
    outlineColor: Color? = null,
    type: ButtonType,
    content: @Composable RowScope.() -> Unit
) {
    val shapes = AppTheme.shapes

    val shape = when (type) {
        ButtonType.SQUARE, ButtonType.SECONDARY_COMPACT, ButtonType.ADD_TRANSACTION -> shapes.large
        else -> shapes.extraLarge
    }

    val height = when (type) {
        ButtonType.SQUARE -> ButtonSize.SquareButton.Dimension
        ButtonType.TEXT -> ButtonSize.TextButton.Height
        ButtonType.SECONDARY_COMPACT -> ButtonSize.CompactButton.Height
        ButtonType.ADD_TRANSACTION -> ButtonSize.AddTransactionButton.Height
        else -> ButtonSize.MainButton.Height
    }

    val baseModifier = when (type) {
        ButtonType.SQUARE -> modifier.size(ButtonSize.SquareButton.Dimension)
        ButtonType.SECONDARY_COMPACT -> modifier.width(ButtonSize.CompactButton.Width)
        ButtonType.ADD_TRANSACTION -> modifier.height(height)
        else -> modifier
            .fillMaxWidth()
            .height(height)
    }

    val buttonModifier = baseModifier
        .background(backgroundColor, shape = shape)
        .then(outlineColor?.let {
            Modifier.border(
                Spacing.Micro,
                it,
                shape
            )
        } ?: Modifier)

    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = buttonModifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        content()
    }
}