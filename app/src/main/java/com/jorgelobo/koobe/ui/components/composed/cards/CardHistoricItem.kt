package com.jorgelobo.koobe.ui.components.composed.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.CategoryHistory
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.category.SubcategoryHistory
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.transaction.Transaction
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.common.AppBadge
import com.jorgelobo.koobe.ui.components.common.MoneyText
import com.jorgelobo.koobe.ui.components.composed.base.BaseExpandableCard
import com.jorgelobo.koobe.ui.components.composed.base.BaseExpandableRow
import com.jorgelobo.koobe.ui.components.composed.lists.ListTransactionItem
import com.jorgelobo.koobe.ui.components.composed.lists.ListTransactionItemConfig
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.color.AccentMint
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor
import java.util.Date

@Composable
fun CardHistoricItem(
    modifier: Modifier = Modifier,
    config: CardHistoricItemConfig
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    val category = config.category
    val categoryHistory = config.categoryHistory

    var isExpanded by remember { mutableStateOf(false) }

    BaseExpandableCard(
        modifier = modifier,
        isExpanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        headerContent = {
            Avatar(
                type = AvatarType.MEDIUM,
                icon = category.icon,
                color = category.resolvedColor(),
                isSelected = false
            )

            Text(
                text = category.name,
                style = typography.text.titleMedium,
                color = colors.textColors.textPrimary,
                modifier = Modifier.padding(start = Spacing.Small, end = Spacing.Tiny)
            )

            AppBadge(
                value = categoryHistory.transactionCount,
                isExpanded = isExpanded
            )

            Spacer(modifier = Modifier.weight(1f))

            MoneyText(
                modifier = Modifier.padding(end = Spacing.Medium),
                amount = categoryHistory.totalAmount,
                currencyType = config.currencyType,
                wholeFontSize = typography.numbers.bodyLarge.fontSize,
                decimalFontSize = typography.numbers.labelMedium.fontSize,
                textColor = if (category.type == TransactionType.INCOME) AccentMint else AccentCoral,
                textAlign = TextAlign.End,
                isEnabled = true
            )
        },
        expandedContent = {
            categoryHistory.subcategories.forEach { subcategoryHistory ->
                val subcategory = subcategoryHistory
                var isSubExpanded by remember { mutableStateOf(false) }

                BaseExpandableRow(
                    isExpanded = isSubExpanded,
                    onExpandedChange = { isSubExpanded = it },
                    headerContent = {
                        Avatar(
                            type = AvatarType.SMALL,
                            icon = subcategory.subcategory.icon,
                            color = category.resolvedColor(),
                            isSelected = false
                        )

                        Text(
                            text = subcategory.subcategory.name,
                            style = typography.text.titleMedium,
                            color = colors.textColors.textPrimary,
                            modifier = Modifier.padding(start = Spacing.Small, end = Spacing.Tiny)
                        )

                        AppBadge(
                            value = subcategoryHistory.transactionCount,
                            isExpanded = isSubExpanded
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        MoneyText(
                            modifier = Modifier.padding(end = Spacing.Medium),
                            amount = subcategoryHistory.totalAmount,
                            currencyType = config.currencyType,
                            wholeFontSize = typography.numbers.bodyLarge.fontSize,
                            decimalFontSize = typography.numbers.labelMedium.fontSize,
                            textColor = if (category.type == TransactionType.INCOME) AccentMint else AccentCoral,
                            textAlign = TextAlign.End,
                            isEnabled = true
                        )
                    },
                    expandedContent = {
                        subcategoryHistory.transactions.forEach { transaction ->
                            ListTransactionItem(
                                config = ListTransactionItemConfig(transaction)
                            )
                        }
                    }
                )
            }
        }
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewCardHistoricItem() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            // Mock Transactions
            val transactionsFood = listOf(
                Transaction(
                    id = 1,
                    date = Date(),
                    description = "Supermarket",
                    type = TransactionType.EXPENSE,
                    categoryId = 1,
                    subcategoryId = 1,
                    amount = -23.45,
                    paymentMethod = PaymentMethodType.CARD,
                    currency = CurrencyType.EUR
                ),
                Transaction(
                    id = 2,
                    date = Date(),
                    description = "Restaurant",
                    type = TransactionType.EXPENSE,
                    categoryId = 1,
                    subcategoryId = 1,
                    amount = -42.10,
                    paymentMethod = PaymentMethodType.CASH,
                    currency = CurrencyType.EUR
                )
            )

            val transactionsGroceries = listOf(
                Transaction(
                    id = 3,
                    date = Date(),
                    description = "Fruits & Veggies",
                    type = TransactionType.EXPENSE,
                    categoryId = 1,
                    subcategoryId = 2,
                    amount = -15.80,
                    paymentMethod = PaymentMethodType.CARD,
                    currency = CurrencyType.EUR
                )
            )

            // Mock Subcategories
            val subcategoryFood = Subcategory(
                id = 1,
                categoryId = 1,
                name = "Restaurants",
                icon = IconPack.RESTAURANT.icon
            )

            val subcategoryGroceries = Subcategory(
                id = 2,
                categoryId = 1,
                name = "Groceries",
                icon = IconPack.FOOD_ITEMS.icon
            )

            // Mock SubcategoryHistory
            val subcategoryHistoryFood = SubcategoryHistory(
                subcategory = subcategoryFood,
                transactionCount = transactionsFood.size,
                totalAmount = transactionsFood.sumOf { it.amount },
                transactions = transactionsFood
            )

            val subcategoryHistoryGroceries = SubcategoryHistory(
                subcategory = subcategoryGroceries,
                transactionCount = transactionsGroceries.size,
                totalAmount = transactionsGroceries.sumOf { it.amount },
                transactions = transactionsGroceries
            )

            // Mock Category
            val categoryFood = Category(
                id = 1,
                name = "Food & Dining",
                icon = IconPack.DINING.icon,
                color = "#FF7043",
                type = TransactionType.EXPENSE,
                subcategories = listOf(subcategoryFood, subcategoryGroceries)
            )

            // Mock CategoryHistory
            val categoryHistory = CategoryHistory(
                category = categoryFood,
                transactionCount = 3,
                totalAmount = transactionsFood.sumOf { it.amount } + transactionsGroceries.sumOf { it.amount },
                subcategories = listOf(subcategoryHistoryFood, subcategoryHistoryGroceries)
            )

            // Final Config
            val config = CardHistoricItemConfig(
                category = categoryFood,
                categoryHistory = categoryHistory,
                currencyType = CurrencyType.EUR
            )

            CardHistoricItem(
                config = config
            )
        }
    }
}