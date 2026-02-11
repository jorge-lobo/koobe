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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
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
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.color.AccentGold
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun CardPeriodicBudgetsItem(
    modifier: Modifier = Modifier,
    config: CardPeriodicBudgetsConfig,
    onItemClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    var isExpanded by remember { mutableStateOf(false) }

    val currencyType = config.currencyType
    val totalLimit = config.totalLimit
    val totalSpent = config.totalSpent
    val balance = totalLimit - totalSpent
    val percentage = if (totalLimit > 0) (totalSpent / totalLimit) else 0.0
    val progress = percentage.toFloat()

    val categoryMap = remember(config.categories) { config.categories.associateBy { it.id } }
    val subcategoryMap =
        remember(config.subcategories) { config.subcategories.associateBy { it.id } }

    val headerLabel = when (config.periodType) {
        PeriodType.DAILY -> stringResource(R.string.budget_header_daily)
        PeriodType.WEEKLY -> stringResource(R.string.budget_header_weekly)
        PeriodType.MONTHLY -> stringResource(R.string.budget_header_monthly)
        PeriodType.YEARLY -> stringResource(R.string.budget_header_yearly)
    }

    BaseExpandableCard(
        modifier = modifier,
        isExpanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        headerContent = {
            Text(
                text = headerLabel,
                style = typography.text.titleMedium,
                color = colors.textColors.textPrimary,
                modifier = Modifier.padding(horizontal = Spacing.Tiny)
            )

            AppBadge(
                value = config.budgetsCount,
                isExpanded = isExpanded,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${(percentage * 100).toInt()}%",
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

                config.budgets.forEachIndexed { index, budget ->
                    val category = categoryMap[budget.categoryId]
                    val subcategory = subcategoryMap[budget.subcategoryId]

                    if (category != null && subcategory != null) {
                        val model = BudgetUiModel(
                            budget = budget,
                            category = category,
                            subcategory = subcategory
                        )

                        BudgetDetailedItem(
                            config = BudgetItemConfig(
                                model = model,
                                onClick = onItemClick
                            ),
                            modifier = Modifier.padding(top = Spacing.Medium, bottom = Spacing.Tiny)
                        )

                        if (index < config.budgets.lastIndex) {
                            AppHorizontalDivider()
                        }
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
                Category(1, "Home", IconPack.HOME.icon, "#FF5722", TransactionType.EXPENSE)
            )

            val subcategories = listOf(
                Subcategory(1, 1, "Internet", IconPack.INTERNET_TV.icon),
                Subcategory(2, 1, "Electricity", IconPack.ELECTRICITY.icon)
            )

            val config = CardPeriodicBudgetsConfig(
                periodType = PeriodType.MONTHLY,
                currencyType = CurrencyType.EUR,
                budgetsCount = 2,
                totalLimit = 500.0,
                totalSpent = 200.0,
                budgets = budgets,
                categories = categories,
                subcategories = subcategories
            )

            CardPeriodicBudgetsItem(
                config = config,
                onItemClick = {}
            )
        }
    }
}