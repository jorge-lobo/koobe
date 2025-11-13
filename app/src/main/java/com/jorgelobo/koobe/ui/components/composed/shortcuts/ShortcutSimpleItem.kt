package com.jorgelobo.koobe.ui.components.composed.shortcuts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconCategory
import com.jorgelobo.koobe.ui.components.model.icons.IconSubcategory
import com.jorgelobo.koobe.ui.components.model.shortcut.ShortcutUiModel
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.ListItemSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun ShortcutSimpleItem(
    modifier: Modifier = Modifier,
    config: ShortcutSimpleItemConfig
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(ListItemSize.MainHeight)
            .padding(horizontal = Spacing.Small)
            .clickable(enabled = config.onClick != null) { config.onClick?.invoke() },
        horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(
            type = AvatarType.MEDIUM,
            icon = config.model.shortcut.icon,
            color = config.model.category.resolvedColor(),
            isSelected = false
        )

        Text(
            text = config.model.shortcut.name,
            style = typography.titleMedium,
            color = colors.textColors.textPrimary
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewShortcutSimpleItem() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            val shortcut = Shortcut(
                id = 1,
                name = "Electricity",
                icon = IconSubcategory.ELECTRICITY.icon,
                categoryId = 1,
                transactionType = TransactionType.EXPENSE,
                paymentMethod = PaymentMethodType.CASH,
                currency = CurrencyType.EUR,
                amount = 30.0,
                repeat = false,
            )

            val category = Category(
                id = 1,
                name = "Home",
                icon = IconCategory.HOME.icon,
                color = "#FF5722",
                type = TransactionType.EXPENSE
            )

            val model = ShortcutUiModel(
                shortcut = shortcut,
                category = category
            )

            ShortcutSimpleItem(
                config = ShortcutSimpleItemConfig(
                    model = model,
                    onClick = {}
                )
            )
        }
    }
}
