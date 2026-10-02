package com.jorgelobo.koobe.ui.components.composed.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.dividers.AppHorizontalDivider
import com.jorgelobo.koobe.ui.components.base.progressBar.AppProgressBar
import com.jorgelobo.koobe.ui.components.base.progressBar.ProgressBarConfig
import com.jorgelobo.koobe.ui.components.common.AmountDisplay
import com.jorgelobo.koobe.ui.components.common.AppBadge
import com.jorgelobo.koobe.ui.components.common.MoneyText
import com.jorgelobo.koobe.ui.components.composed.base.BaseExpandableCard
import com.jorgelobo.koobe.ui.components.composed.budgets.BudgetDetailedItem
import com.jorgelobo.koobe.ui.components.composed.budgets.BudgetItemConfig
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.budgets.manager.model.PeriodicBudgetsUiModel
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.color.AccentGold
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun CardPeriodicBudgetsItem(
    modifier: Modifier = Modifier,
    config: CardPeriodicBudgetsConfig
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    val model = config.model

    val currencyType = model.currencyType
    val totalLimit = model.totalLimit
    val totalSpent = model.totalSpent
    val balance = model.balance
    val percentage = model.percentage
    val progress = model.progress

    val headerLabel = when (model.periodType) {
        PeriodType.DAILY -> stringResource(R.string.budget_header_daily)
        PeriodType.WEEKLY -> stringResource(R.string.budget_header_weekly)
        PeriodType.MONTHLY -> stringResource(R.string.budget_header_monthly)
        PeriodType.YEARLY -> stringResource(R.string.budget_header_yearly)
    }

    BaseExpandableCard(
        modifier = modifier,
        isExpanded = model.isExpanded,
        onExpandedChange = { config.onExpandToggle() },
        headerContent = {
            Text(
                text = headerLabel,
                style = typography.text.titleMedium,
                color = colors.textColors.textPrimary,
                modifier = Modifier.padding(horizontal = Spacing.Tiny)
            )

            AppBadge(
                value = model.budgetsCount,
                isExpanded = model.isExpanded,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${percentage.toInt()}%",
                style = typography.numbers.labelMedium,
                color = AccentGold,
                modifier = Modifier.padding(end = Spacing.Small)
            )

            AmountDisplay(
                amount = balance,
                currencyType = currencyType
            )

            Spacer(modifier = Modifier.width(Spacing.Small))
        },
        expandedContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Spacing.Small, end = Spacing.Small, bottom = Spacing.Small),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    MoneyText(
                        amount = totalSpent,
                        currencyType = currencyType,
                        wholeFontSize = typography.numbers.labelMedium.fontSize,
                        decimalFontSize = typography.numbers.labelSmall.fontSize,
                        textColor = if (totalSpent > totalLimit) AccentCoral else colors.textColors.textSecondary,
                        textAlign = TextAlign.End,
                        isEnabled = true
                    )

                    Text(
                        text = "/",
                        style = typography.numbers.labelMedium,
                        color = colors.textColors.textSecondary
                    )

                    MoneyText(
                        amount = totalLimit,
                        currencyType = currencyType,
                        wholeFontSize = typography.numbers.labelMedium.fontSize,
                        decimalFontSize = typography.numbers.labelSmall.fontSize,
                        textColor = colors.textColors.textSecondary,
                        textAlign = TextAlign.End,
                        isEnabled = true
                    )
                }

                AppProgressBar(
                    config = ProgressBarConfig(
                        progress = progress,
                        projection = 0f,
                        percentageLabel = "",
                    ),
                    modifier = Modifier.padding(top = Spacing.Tiny)
                )

                model.budgets.forEachIndexed { index, budget ->
                    BudgetDetailedItem(
                        config = BudgetItemConfig(
                            model = budget,
                            onClick = { config.onItemClick(budget) }
                        ),
                        modifier = Modifier.padding(top = Spacing.Medium, bottom = Spacing.Tiny)
                    )

                    if (index < model.budgets.lastIndex) {
                        AppHorizontalDivider()
                    }
                }
            }
        }
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewCardPeriodicBudgetsItem() {
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
            val budgets = listOf(
                Budget(
                    id = 1,
                    categoryId = 1,
                    subcategoryId = 1,
                    period = PeriodType.MONTHLY,
                    repeat = false,
                    paymentMethod = null,
                    currency = CurrencyType.EUR,
                    limitAmount = 200.0,
                    spentAmount = 50.0,
                    projectedAmount = 150.0,
                    dailyAverage = 10.0
                ),
                Budget(
                    id = 2,
                    categoryId = 1,
                    subcategoryId = 2,
                    period = PeriodType.MONTHLY,
                    repeat = false,
                    paymentMethod = null,
                    currency = CurrencyType.EUR,
                    limitAmount = 300.0,
                    spentAmount = 150.0,
                    projectedAmount = 250.0,
                    dailyAverage = 17.50
                )
            )

            val categories = listOf(
                Category(1, "Home", IconPack.HOME, "#FF5722", TransactionType.EXPENSE)
            )

            val subcategories = listOf(
                Subcategory(1, 1, "Internet", IconPack.INTERNET_TV),
                Subcategory(2, 1, "Electricity", IconPack.ELECTRICITY)
            )

            val model = PeriodicBudgetsUiModel(
                periodType = PeriodType.MONTHLY,
                currencyType = CurrencyType.EUR,
                budgets = budgets.map {
                    BudgetUiModel.from(
                        it,
                        categories.first { category -> category.id == it.categoryId },
                        subcategories.first { subcategory -> subcategory.id == it.subcategoryId }
                    )
                },
                totalLimit = 500.0,
                totalSpent = 200.0
            )

            val config = CardPeriodicBudgetsConfig(
                model = model,
                onExpandToggle = {},
                onItemClick = {}
            )

            CardPeriodicBudgetsItem(config = config)
        }
    }
}