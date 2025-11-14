package com.jorgelobo.koobe.ui.components.composed.lists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.dividers.AppHorizontalDivider
import com.jorgelobo.koobe.ui.components.common.MoneyText
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.DateFormat
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.color.AccentGold
import com.jorgelobo.koobe.ui.theme.color.AccentMint
import com.jorgelobo.koobe.ui.theme.dimens.IconSize
import com.jorgelobo.koobe.ui.theme.dimens.ListItemSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.DateUtils
import com.jorgelobo.koobe.utils.DateUtils.formatAs
import com.jorgelobo.koobe.utils.paymentMethodIcon
import com.jorgelobo.koobe.R

@Composable
fun ListTransactionItem(
    modifier: Modifier = Modifier,
    config: ListTransactionItemConfig
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    val transaction = config.transaction
    val icon = remember(transaction.paymentMethod) { paymentMethodIcon(transaction.paymentMethod) }
    val formattedDate =
        remember(transaction.date) { transaction.date.formatAs(DateFormat.DAY_MONTH) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(ListItemSize.SecondaryHeight)
            .clickable(enabled = config.onClick != null) { config.onClick?.invoke() },
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.Micro),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formattedDate,
                style = typography.text.bodySmall,
                color = colors.textColors.textSecondary,
                modifier = Modifier.width(Spacing.ExtraLarge)
            )

            Text(
                text = transaction.description,
                style = typography.text.bodyLarge,
                color = colors.textColors.textPrimary,
                modifier = Modifier.weight(1f)
            )

            MoneyText(
                amount = transaction.amount,
                currencyType = transaction.currency,
                wholeFontSize = typography.numbers.labelLarge.fontSize,
                decimalFontSize = typography.numbers.labelSmall.fontSize,
                textColor = if (transaction.type == TransactionType.INCOME) AccentMint else AccentCoral,
                textAlign = TextAlign.End,
                isEnabled = true,
                modifier = Modifier.padding(end = Spacing.Medium)
            )

            Icon(
                imageVector = icon,
                contentDescription = stringResource(R.string.cd_payment),
                tint = AccentGold,
                modifier = Modifier.size(IconSize.ExtraSmall)
            )
        }

        AppHorizontalDivider()
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewTransactionItem() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            val transaction = Transaction(
                id = 1,
                date = DateUtils.currentDate,
                description = "Payment",
                type = TransactionType.EXPENSE,
                categoryId = 1,
                subcategoryId = 1,
                amount = 30.0,
                paymentMethod = PaymentMethodType.CASH,
                currency = CurrencyType.EUR
            )

            ListTransactionItem(
                config = ListTransactionItemConfig(
                    transaction = transaction,
                    onClick = {}
                )
            )
        }
    }
}