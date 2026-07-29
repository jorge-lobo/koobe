package com.jorgelobo.koobe.ui.components.base.buttons.types

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.color.Transparent
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun ButtonText(
    onClick: () -> Unit,
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

    Row(
        modifier = Modifier
            .wrapContentWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .background(color = Transparent)
            .padding(vertical = Spacing.MediumSmall),
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

            Spacer(modifier = Modifier.width(Spacing.ExtraSmall))
        }
        Text(
            text = text,
            color = resolvedTextColor,
            style = AppTheme.typography.text.bodyLarge
        )
    }
}