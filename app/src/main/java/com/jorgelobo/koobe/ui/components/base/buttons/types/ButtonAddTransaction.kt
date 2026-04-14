package com.jorgelobo.koobe.ui.components.base.buttons.types

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.ButtonSize
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.getTransactionButtonData

@Composable
fun ButtonAddTransaction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    type: TransactionType
) {
    val colors = AppTheme.colors.buttonColors
    val shape = AppTheme.shapes.medium

    val buttonData = getTransactionButtonData(type)

    Surface(
        modifier = modifier
            .height(ButtonSize.AddTransactionButton.Height),
        shape = shape,
        color = buttonData.color,
        border = null,
        enabled = enabled,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = buttonData.icon.icon,
                modifier = Modifier.size(IconSize.ExtraSmall),
                contentDescription = buttonData.contextDescription,
                tint = AppTheme.colors.iconColors.iconAvatar
            )

            Spacer(modifier = Modifier.padding(Spacing.Small))

            Text(
                text = buttonData.text,
                color = colors.buttonPrimaryLabelText,
                style = AppTheme.typography.text.bodyLarge
            )
        }
    }
}