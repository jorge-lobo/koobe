package com.jorgelobo.koobe.ui.components.composed.grids

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.grid.GridItemUiModel
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.components.model.shortcut.ShortcutUiModel
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun ShortcutsGrid(
    modifier: Modifier = Modifier,
    config: ShortcutsGridConfig
) {
    SelectableGridSection(
        modifier = modifier,
        title = stringResource(R.string.title_user_shortcuts),
        items = config.list.map {
            GridItemUiModel(
                id = it.shortcut.id,
                name = it.shortcut.name,
                icon = it.shortcut.icon,
                color = it.category.resolvedColor()
            )
        },
        selectedId = config.selectedShortcutId,
        onItemClick = config.onShortcutClick
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewShortcutsGrid() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            var selectedShortcutId by remember { mutableStateOf<Int?>(null) }

            val mockShortcuts = listOf(
                Shortcut(
                    id = 1,
                    name = "Electricity",
                    icon = IconPack.ELECTRICITY.icon,
                    categoryId = 1,
                    transactionType = TransactionType.EXPENSE,
                    paymentMethod = PaymentMethodType.CASH,
                    currency = CurrencyType.EUR,
                    amount = 30.0,
                    repeat = false,
                ),
                Shortcut(
                    id = 2,
                    name = "Internet & TV",
                    icon = IconPack.INTERNET_TV.icon,
                    categoryId = 1,
                    transactionType = TransactionType.EXPENSE,
                    paymentMethod = PaymentMethodType.CASH,
                    currency = CurrencyType.EUR,
                    amount = 30.0,
                    repeat = false,
                ),
                Shortcut(
                    id = 3,
                    name = "Restaurant",
                    icon = IconPack.RESTAURANT.icon,
                    categoryId = 2,
                    transactionType = TransactionType.EXPENSE,
                    paymentMethod = PaymentMethodType.CASH,
                    currency = CurrencyType.EUR,
                    amount = 30.0,
                    repeat = false,
                ),
                Shortcut(
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
            )

            val category =
                Category(1, "Home", IconPack.HOME.icon, "#FFB74D", TransactionType.EXPENSE)

            ShortcutsGrid(
                config = ShortcutsGridConfig(
                    list = mockShortcuts.map { ShortcutUiModel(it, category) },
                    selectedShortcutId = selectedShortcutId,
                    onShortcutClick = { selectedShortcutId = it }
                )
            )
        }
    }
}