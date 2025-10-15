package com.jorgelobo.koobe.ui.components.base.buttons.types

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonBase
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.theme.AppTheme

@Composable
fun ButtonSecondary(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    text: String
) {
    val buttonColors = AppTheme.colors.buttonColors

    ButtonBase(
        onClick = onClick,
        modifier = modifier,
        backgroundColor = buttonColors.buttonSecondaryContainer,
        outlineColor = buttonColors.buttonSecondaryOutline,
        type = ButtonType.SECONDARY,
        isEnabled = enabled
    ) {
        Text(
            text = text,
            color = buttonColors.buttonSecondaryLabelText,
            style = AppTheme.typography.text.titleLarge
        )
    }
}