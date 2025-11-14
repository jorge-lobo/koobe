package com.jorgelobo.koobe.ui.components.base.buttons.types

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
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonBase
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.theme.AppTheme
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

    val buttonData = getTransactionButtonData(type)

    ButtonBase(
        onClick = onClick,
        modifier = modifier,
        backgroundColor = buttonData.color,
        outlineColor = buttonData.color,
        type = ButtonType.ADD_TRANSACTION,
        isEnabled = enabled
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = buttonData.icon,
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