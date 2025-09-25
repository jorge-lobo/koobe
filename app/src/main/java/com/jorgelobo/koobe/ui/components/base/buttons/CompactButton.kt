package com.jorgelobo.koobe.ui.components.base.buttons

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.ui.components.model.ButtonType
import com.jorgelobo.koobe.ui.theme.AppTheme

@Composable
fun CompactButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    text: String
) {
    val buttonColors = AppTheme.colors.buttonColors

    BaseButton(
        onClick = onClick,
        modifier = modifier,
        backgroundColor = buttonColors.buttonSecondaryContainer,
        outlineColor = buttonColors.buttonSecondaryOutline,
        type = ButtonType.SECONDARY_COMPACT,
        isEnabled = enabled
    ) {
        Text(
            text = text,
            color = buttonColors.buttonSecondaryLabelText,
            style = AppTheme.typography.text.bodyLarge
        )
    }
}