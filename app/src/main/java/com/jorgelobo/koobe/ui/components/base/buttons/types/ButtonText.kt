package com.jorgelobo.koobe.ui.components.base.buttons.types

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonBase
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.color.Transparent
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun ButtonText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color? = null,
    enabled: Boolean,
    icon: IconPack? = null
) {
    val buttonColors = AppTheme.colors.buttonColors
    val iconColor = AppTheme.colors.iconColors

    val resolvedTextColor = when {
        !enabled -> buttonColors.buttonDisabledLabelText
        textColor != null -> textColor
        else -> buttonColors.buttonTextDefault
    }

    ButtonBase(
        onClick = onClick,
        modifier = modifier,
        backgroundColor = Transparent,
        outlineColor = Transparent,
        type = ButtonType.TEXT,
        isEnabled = enabled
    ) {
        Row(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon.icon,
                    modifier = Modifier.size(IconSize.Small),
                    contentDescription = stringResource(R.string.cd_button_icon),
                    tint = iconColor.iconTextButton
                )

                Spacer(modifier = Modifier.padding(Spacing.ExtraSmall))
            }
            Text(
                text = text,
                color = resolvedTextColor,
                style = AppTheme.typography.text.bodyLarge
            )
        }
    }
}