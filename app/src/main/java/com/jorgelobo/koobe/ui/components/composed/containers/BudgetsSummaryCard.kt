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
import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.cards.BaseDashboardCard
import com.jorgelobo.koobe.ui.components.composed.budgets.BudgetItemConfig
import com.jorgelobo.koobe.ui.components.composed.budgets.BudgetSimpleItem
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.DashboardCardType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BudgetsSummaryCard(
    modifier: Modifier = Modifier,
    config: BudgetsSummaryCardConfig
) {
    BaseDashboardCard(
        modifier = modifier,
        type = DashboardCardType.BUDGETS,
        isEmptyState = config.items.isEmpty(),
        onClick = config.onActionClick
    ) {
        if (config.items.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.Small),
                verticalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                items(config.items.take(2)) { item ->
                    BudgetSimpleItem(
                        config = BudgetItemConfig(
                            model = item,
                            onClick = { config.onBudgetClick(item) }
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetsSummaryCardPreview() {
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
            val sampleBudgets = remember { sampleBudgetUiModels() }

            BudgetsSummaryCard(
                config = BudgetsSummaryCardConfig(
                    items = sampleBudgets,
                    onBudgetClick = {},
                    onActionClick = {}
                )
            )
        }
    }
}

fun sampleBudgetUiModels(): List<BudgetUiModel> {
    val categoryFood = Category(
        id = 1,
        name = "Dining",
        icon = IconPack.DINING,
        color = "#FFB74D",
        type = TransactionType.EXPENSE
    )

    val subcategoryDining = Subcategory(
        id = 11,
        categoryId = 1,
        name = "Restaurant",
        icon = IconPack.RESTAURANT
    )

    val budget1 = Budget(
        id = 101,
        categoryId = 1,
        subcategoryId = 11,
        period = PeriodType.MONTHLY,
        repeat = true,
        paymentMethod = null,
        currency = CurrencyType.EUR,
        limitAmount = 200.0,
        spentAmount = 150.0,
        projectedAmount = 190.0,
        dailyAverage = 7.2
    )

    val budget2 = budget1.copy(
        id = 102,
        subcategoryId = 12,
        limitAmount = 300.0,
        spentAmount = 280.0,
        projectedAmount = 310.0
    )

    val budget3 = budget1.copy(
        id = 103,
        subcategoryId = 13,
        limitAmount = 100.0,
        spentAmount = 95.0,
        projectedAmount = 98.0
    )

    return listOf(
        BudgetUiModel(budget1, categoryFood, subcategoryDining),
        BudgetUiModel(budget2, categoryFood, subcategoryDining),
        BudgetUiModel(budget3, categoryFood, subcategoryDining)
    )
}