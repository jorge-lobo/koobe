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
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.payment.PaymentMethod
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.common.MoneyText
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.ReportItemType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.color.AccentGold
import com.jorgelobo.koobe.ui.theme.color.AccentMint
import com.jorgelobo.koobe.ui.theme.dimens.AmountDisplaySize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.paymentMethodIcon
import com.jorgelobo.koobe.utils.resolvedColor
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
    val defaultIcon = IconPack.EXTRA
    val defaultColor = colors.containerColors.avatarContainerDefault

    val (icon, color, name) = when (config.type) {
        ReportItemType.CATEGORY -> Triple(
            category?.icon ?: defaultIcon,
            category?.resolvedColor() ?: defaultColor,
            category?.name ?: stringResource(R.string.default_category)
        )

        ReportItemType.PAYMENT_METHOD -> Triple(
            paymentMethod?.let { paymentMethodIcon(it.type) } ?: defaultIcon,
            paymentMethod?.resolvedColor() ?: defaultColor,
            paymentMethod?.name ?: stringResource(R.string.default_payment_method)
        )
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
            text = String.format(Locale.getDefault(), "%.1f%%", config.percentage * 100),
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
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
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
                icon = IconPack.HOME,
                color = "#FF5722",
                type = TransactionType.EXPENSE,
                subcategories = emptyList()
            )

            val paymentMethod = PaymentMethod(
                type = PaymentMethodType.CARD,
                name = "Card",
                icon = IconPack.CASH,
                color = "#452136"
            )

            val config1 = ReportItemRowConfig(
                type = ReportItemType.CATEGORY,
                category = category,
                paymentMethod = null,
                currencyType = CurrencyType.EUR,
                transactionType = TransactionType.INCOME,
                percentage = 0.253,
                amount = 390.0
            )

            val config2 = ReportItemRowConfig(
                type = ReportItemType.PAYMENT_METHOD,
                category = null,
                paymentMethod = paymentMethod,
                currencyType = CurrencyType.EUR,
                transactionType = TransactionType.EXPENSE,
                percentage = 0.65,
                amount = 42353.25
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