package com.jorgelobo.koobe.ui.components.base.buttons.types

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonBase
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.theme.AppTheme

@Composable
fun ButtonPrimary(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    text: String
) {
    val buttonColors = AppTheme.colors.buttonColors
    val disabledColor = buttonColors.buttonDisabledContainer

    ButtonBase(
        onClick = onClick,
        modifier = modifier,
        backgroundColor = if (enabled) buttonColors.buttonPrimaryContainer else disabledColor,
        outlineColor = if (enabled) buttonColors.buttonPrimaryContainer else disabledColor,
        type = ButtonType.PRIMARY,
        isEnabled = enabled
    ) {
        Text(
            text = text,
            color = if (enabled) buttonColors.buttonPrimaryLabelText else buttonColors.buttonDisabledLabelText,
            style = AppTheme.typography.text.titleLarge
        )
    }
}