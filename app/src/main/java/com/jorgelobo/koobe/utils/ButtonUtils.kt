package com.jorgelobo.koobe.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.model.button.TransactionButtonData
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.icons.getIconFromName
import com.jorgelobo.koobe.ui.theme.AppTheme

@Composable
fun getTransactionButtonData(type: TransactionType): TransactionButtonData {
    val colors = AppTheme.colors

    return when (type) {
        TransactionType.INCOME -> TransactionButtonData(
            icon = getIconFromName(IconGeneral.ADD),
            color = colors.buttonColors.buttonAddIncomeContainer,
            text = stringResource(R.string.btn_add_income),
            contextDescription = stringResource(R.string.cd_add_income)
        )

        TransactionType.EXPENSE -> TransactionButtonData(
            icon = getIconFromName(IconGeneral.MINUS),
            color = colors.buttonColors.buttonAddExpenseContainer,
            text = stringResource(R.string.btn_add_expense),
            contextDescription = stringResource(R.string.cd_add_expense)
        )
    }
}