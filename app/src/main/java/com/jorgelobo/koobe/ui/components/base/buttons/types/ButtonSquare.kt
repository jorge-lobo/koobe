package com.jorgelobo.koobe.ui.components.base.buttons.types

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonBase
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.R

@Composable
fun ButtonSquare(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    iconUrl: ImageVector
) {
    val buttonColors = AppTheme.colors.buttonColors
    val iconColor = AppTheme.colors.iconColors

    ButtonBase(
        onClick = onClick,
        modifier = modifier,
        backgroundColor = buttonColors.buttonSquareContainer,
        outlineColor = buttonColors.buttonSquareOutline,
        type = ButtonType.SQUARE,
        isEnabled = enabled
    ) {
        Icon(
            imageVector = iconUrl,
            contentDescription = stringResource(R.string.cd_button_icon),
            tint = if (enabled) iconColor.iconPrimary else iconColor.iconDisabled
        )
    }
}