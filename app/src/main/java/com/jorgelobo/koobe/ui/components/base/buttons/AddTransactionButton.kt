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
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.TransactionType
import com.jorgelobo.koobe.ui.components.model.ButtonType
import com.jorgelobo.koobe.ui.components.model.IconName
import com.jorgelobo.koobe.ui.icons.getIconFromName
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun AddTransactionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    type: TransactionType
) {
    val colors = AppTheme.colors.buttonColors
    val icon = when (type) {
        TransactionType.INCOME -> getIconFromName(IconName.ADD)
        TransactionType.EXPENSE -> getIconFromName(IconName.MINUS)
    }
    val buttonColor = when (type) {
        TransactionType.INCOME -> colors.buttonAddIncomeContainer
        TransactionType.EXPENSE -> colors.buttonAddExpenseContainer
    }
    val text = when (type) {
        TransactionType.INCOME -> stringResource(R.string.btn_add_income)
        TransactionType.EXPENSE -> stringResource(R.string.btn_add_expense)
    }

    BaseButton(
        onClick = onClick,
        modifier = modifier,
        backgroundColor = buttonColor,
        outlineColor = buttonColor,
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
                imageVector = icon,
                modifier = Modifier.size(IconSize.ExtraSmall),
                contentDescription = null,
                tint = AppTheme.colors.iconColors.iconAvatar
            )

            Spacer(modifier = Modifier.padding(Spacing.Small))

            Text(
                text = text,
                color = colors.buttonPrimaryLabelText,
                style = AppTheme.typography.text.bodyLarge
            )
        }
    }
}