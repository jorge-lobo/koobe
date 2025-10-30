package com.jorgelobo.koobe.ui.components.composed.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.payment.PaymentMethod
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.common.MoneyText
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconCategory
import com.jorgelobo.koobe.ui.components.model.icons.IconPayment
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.color.AccentGold
import com.jorgelobo.koobe.ui.theme.color.AccentMint
import com.jorgelobo.koobe.ui.theme.dimens.AmountDisplaySize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolveColor
import com.jorgelobo.koobe.R
import java.util.Locale

@Composable
fun ReportItemRow(
    modifier: Modifier = Modifier,
    config: ReportItemRowConfig
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    val category = config.category
    val paymentMethod = config.paymentMethod
    val isCategory = config.isCategory
    val defaultIcon = IconCategory.EXTRA.icon
    val defaultColor = colors.containerColors.avatarContainerDefault

    val icon = when {
        isCategory -> category?.icon ?: defaultIcon
        else -> paymentMethod?.icon ?: defaultIcon
    }

    val color = when {
        isCategory -> category?.resolveColor() ?: defaultColor
        else -> paymentMethod?.resolveColor() ?: defaultColor
    }

    val name = when {
        isCategory -> category?.name ?: stringResource(R.string.default_category)
        else -> paymentMethod?.name ?: stringResource(R.string.default_payment_method)
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Avatar(
            type = AvatarType.MEDIUM,
            icon = icon,
            color = color,
            isSelected = false
        )

        Text(
            text = name,
            style = typography.text.titleMedium,
            color = colors.textColors.textPrimary,
            modifier = Modifier
                .weight(1f)
                .padding(start = Spacing.Small)
        )

        Text(
            text = String.format(Locale.getDefault(),"%.1f%%", config.percentage * 100),
            style = typography.numbers.labelMedium,
            color = AccentGold,
            modifier = Modifier.padding(end = Spacing.Small)
        )

        MoneyText(
            modifier = Modifier.width(AmountDisplaySize.Width),
            amount = config.amount,
            currencyType = config.currencyType,
            wholeFontSize = typography.numbers.bodyLarge.fontSize,
            decimalFontSize = typography.numbers.labelMedium.fontSize,
            textColor = if (config.transactionType == TransactionType.INCOME) AccentMint else AccentCoral,
            textAlign = TextAlign.End,
            isEnabled = true
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewReportItemRow() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            val category = Category(
                id = 1,
                name = "Home",
                icon = IconCategory.HOME.icon,
                color = "#FF5722",
                type = TransactionType.EXPENSE,
                subcategories = emptyList()
            )

            val paymentMethod = PaymentMethod(
                type = PaymentMethodType.CARD,
                name = "Card",
                icon = IconPayment.CARD.icon,
                color = "#452136"
            )

            val config1 = ReportItemRowConfig(
                category = category,
                paymentMethod = null,
                currencyType = CurrencyType.EUR,
                transactionType = TransactionType.INCOME,
                percentage = 0.253,
                amount = 390.0,
                isCategory = true
            )

            val config2 = ReportItemRowConfig(
                category = null,
                paymentMethod = paymentMethod,
                currencyType = CurrencyType.EUR,
                transactionType = TransactionType.EXPENSE,
                percentage = 0.65,
                amount = 42353.25,
                isCategory = false
            )

            ReportItemRow(
                config = config1
            )

            ReportItemRow(
                config = config2
            )
        }
    }
}