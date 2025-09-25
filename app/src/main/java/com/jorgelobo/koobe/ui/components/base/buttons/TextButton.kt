package com.jorgelobo.koobe.ui.components.base.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.ui.components.model.ButtonType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun TextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean,
    iconUrl: ImageVector? = null
) {
    val buttonColors = AppTheme.colors.buttonColors
    val iconColor = AppTheme.colors.iconColors

    BaseButton(
        onClick = onClick,
        modifier = modifier,
        backgroundColor = Color.Transparent,
        outlineColor = Color.Transparent,
        type = ButtonType.TEXT,
        isEnabled = enabled
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconUrl != null) {
                Icon(
                    imageVector = iconUrl,
                    modifier = Modifier.size(IconSize.Small),
                    contentDescription = null,
                    tint = iconColor.iconTextButton
                )

                Spacer(modifier = Modifier.padding(Spacing.ExtraSmall))
            }
            Text(
                text = text,
                color = if (enabled) buttonColors.buttonTextDefault else buttonColors.buttonDisabledLabelText,
                style = AppTheme.typography.text.bodyLarge
            )
        }
    }
}