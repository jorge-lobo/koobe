package com.jorgelobo.koobe.ui.components.composed.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.cards.BaseDashboardCard
import com.jorgelobo.koobe.ui.components.composed.shortcuts.ShortcutSimpleItem
import com.jorgelobo.koobe.ui.components.composed.shortcuts.ShortcutSimpleItemConfig
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.DashboardCardType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.components.model.shortcut.ShortcutUiModel
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun ShortcutsSummaryCard(
    modifier: Modifier = Modifier,
    config: ShortcutsSummaryCardConfig
) {
    BaseDashboardCard(
        modifier = modifier,
        type = DashboardCardType.SHORTCUTS,
        isEmptyState = config.items.isEmpty(),
        onClick = config.onActionClick
    ) {
        if (config.items.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.Small)
            ) {
                items(config.items.take(3)) { item ->
                    ShortcutSimpleItem(
                        config = ShortcutSimpleItemConfig(
                            model = item,
                            onClick = { config.onShortcutClick(item) }
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShortcutsSummaryCardPreview() {
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
            val sampleShortcuts = remember { sampleShortcutUiModels() }

            ShortcutsSummaryCard(
                config = ShortcutsSummaryCardConfig(
                    items = sampleShortcuts,
                    onShortcutClick = {},
                    onActionClick = {}
                )
            )
        }
    }
}

private fun sampleShortcutUiModels(): List<ShortcutUiModel> {
    val shortcutElectricity = Shortcut(
        id = 1,
        name = "Electricity",
        icon = IconPack.ELECTRICITY.icon,
        categoryId = 1,
        transactionType = TransactionType.EXPENSE,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR,
        amount = 30.0,
        repeat = false,
    )

    val shortcutInternetTv = Shortcut(
        id = 2,
        name = "Internet & TV",
        icon = IconPack.INTERNET_TV.icon,
        categoryId = 1,
        transactionType = TransactionType.EXPENSE,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR,
        amount = 30.0,
        repeat = false,
    )

    val shortcutRestaurant = Shortcut(
        id = 3,
        name = "Restaurant",
        icon = IconPack.RESTAURANT.icon,
        categoryId = 2,
        transactionType = TransactionType.EXPENSE,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR,
        amount = 30.0,
        repeat = false,
    )

    val shortcutCanteen = Shortcut(
        id = 4,
        name = "Canteen",
        icon = IconPack.CANTEEN.icon,
        categoryId = 2,
        transactionType = TransactionType.EXPENSE,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR,
        amount = 30.0,
        repeat = false,
    )

    val categoryHome = Category(
        id = 1,
        name = "Home",
        icon = IconPack.HOME.icon,
        color = "#FF5722",
        type = TransactionType.EXPENSE
    )

    val categoryDining = Category(
        id = 2,
        name = "Dining",
        icon = IconPack.DINING.icon,
        color = "#FF5722",
        type = TransactionType.EXPENSE
    )

    return listOf(
        ShortcutUiModel(shortcutElectricity, categoryHome),
        ShortcutUiModel(shortcutInternetTv, categoryHome),
        ShortcutUiModel(shortcutRestaurant, categoryDining),
        ShortcutUiModel(shortcutCanteen, categoryDining)
    )
}