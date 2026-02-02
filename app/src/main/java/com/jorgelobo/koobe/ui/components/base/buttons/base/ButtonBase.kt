package com.jorgelobo.koobe.ui.components.base.buttons.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens
import com.jorgelobo.koobe.ui.theme.dimens.ButtonSize

@Composable
fun ButtonBase(
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
        ButtonType.SQUARE -> shapes.medium
        else -> shapes.extraLarge
    }

    val height = when (type) {
        ButtonType.SQUARE -> ButtonSize.SquareButton.Dimension
        ButtonType.TEXT -> ButtonSize.TextButton.Height
        else -> ButtonSize.MainButton.Height
    }

    val baseModifier = when (type) {
        ButtonType.SQUARE -> modifier.size(ButtonSize.SquareButton.Dimension)
        ButtonType.TEXT -> modifier.wrapContentWidth()
        else -> modifier
            .fillMaxWidth()
            .height(height)
    }

    val buttonModifier = baseModifier
        .background(backgroundColor, shape = shape)
        .then(outlineColor?.let {
            Modifier.border(
                BorderDimens.Base,
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
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        content()
    }
}