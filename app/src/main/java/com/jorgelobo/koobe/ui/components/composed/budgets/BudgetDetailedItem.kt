package com.jorgelobo.koobe.ui.components.composed.budgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.progressBar.AppProgressBar
import com.jorgelobo.koobe.ui.components.base.progressBar.ProgressBarConfig
import com.jorgelobo.koobe.ui.components.common.AmountDisplay
import com.jorgelobo.koobe.ui.components.common.MoneyText
import com.jorgelobo.koobe.ui.components.common.StatusIndicator
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconCategory
import com.jorgelobo.koobe.ui.components.model.icons.IconSubcategory
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.AvatarSize
import com.jorgelobo.koobe.ui.theme.dimens.BudgetItemSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun BudgetDetailedItem(
    modifier: Modifier = Modifier,
    config: BudgetItemConfig
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    val categoryColor = config.model.category.resolvedColor()
    val spent = config.model.budget.spentAmount
    val limit = config.model.budget.limitAmount
    val projected = config.model.budget.projectedAmount
    val spentPercentage = if (limit > 0) (spent / limit) else 0.0
    val projectedPercentage = if (limit > 0) (projected / limit) else 0.0
    val dailyAverage = config.model.budget.dailyAverage
    val balance = limit - spent

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(BudgetItemSize.BudgetDetailed)
            .clickable(enabled = config.onClick != null) { config.onClick?.invoke() },
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            Avatar(
                type = AvatarType.EXTRA_LARGE,
                icon = config.model.subcategory.icon,
                color = categoryColor,
                isSelected = false
            )

            Column(
                modifier = Modifier.height(AvatarSize.ExtraLarge),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = config.model.subcategory.name,
                        style = typography.text.titleMedium,
                        color = colors.textColors.textPrimary
                    )

                    AmountDisplay(
                        amount = balance,
                        currencyType = config.model.budget.currency
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.width(AvatarSize.ExtraLarge),
                        verticalArrangement = Arrangement.spacedBy(Spacing.Tiny),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(R.string.budget_label_daily_average),
                            style = typography.text.bodySmall,
                            color = colors.textColors.textSupportMessage
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.budget_label_projection),
                                style = typography.text.bodySmall,
                                color = colors.textColors.textSupportMessage
                            )

                            StatusIndicator(
                                percentage = projectedPercentage
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(Spacing.Small))

                    Column(
                        modifier = Modifier.wrapContentWidth(),
                        verticalArrangement = Arrangement.spacedBy(Spacing.Tiny),
                        horizontalAlignment = Alignment.End
                    ) {
                        MoneyText(
                            amount = dailyAverage,
                            currencyType = config.model.budget.currency,
                            wholeFontSize = typography.numbers.labelLarge.fontSize,
                            decimalFontSize = typography.numbers.labelSmall.fontSize,
                            textColor = colors.textColors.textSupportMessage,
                            textAlign = TextAlign.End,
                            isEnabled = true
                        )

                        MoneyText(
                            amount = projected,
                            currencyType = config.model.budget.currency,
                            wholeFontSize = typography.numbers.labelLarge.fontSize,
                            decimalFontSize = typography.numbers.labelSmall.fontSize,
                            textColor = colors.textColors.textSupportMessage,
                            textAlign = TextAlign.End,
                            isEnabled = true
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Column(
                        modifier = Modifier.wrapContentWidth(),
                        verticalArrangement = Arrangement.spacedBy(Spacing.Tiny),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = stringResource(R.string.budget_label_budget),
                            style = typography.text.bodySmall,
                            color = colors.textColors.textSupportMessage
                        )

                        Text(
                            text = stringResource(R.string.budget_label_spent),
                            style = typography.text.bodySmall,
                            color = colors.textColors.textSupportMessage
                        )
                    }

                    Spacer(modifier = Modifier.width(Spacing.Small))

                    Column(
                        modifier = Modifier.width(AvatarSize.ExtraLarge),
                        verticalArrangement = Arrangement.spacedBy(Spacing.Tiny),
                        horizontalAlignment = Alignment.End
                    ) {
                        MoneyText(
                            amount = limit,
                            currencyType = config.model.budget.currency,
                            wholeFontSize = typography.numbers.labelLarge.fontSize,
                            decimalFontSize = typography.numbers.labelSmall.fontSize,
                            textColor = colors.textColors.textSupportMessage,
                            textAlign = TextAlign.End,
                            isEnabled = true
                        )

                        MoneyText(
                            amount = spent,
                            currencyType = config.model.budget.currency,
                            wholeFontSize = typography.numbers.labelLarge.fontSize,
                            decimalFontSize = typography.numbers.labelSmall.fontSize,
                            textColor = colors.textColors.textSupportMessage,
                            textAlign = TextAlign.End,
                            isEnabled = true
                        )
                    }
                }
            }
        }

        AppProgressBar(
            config = ProgressBarConfig(
                progress = (spent / limit).toFloat().coerceIn(0f, 1f),
                projection = (projected / limit).toFloat().coerceIn(0f, 1f),
                percentageLabel = "${(spentPercentage * 100).toInt()}%"
            )
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewBudgetDetailedItem() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            val budget = Budget(
                id = 1,
                categoryId = 1,
                subcategoryId = 1,
                period = PeriodType.MONTHLY,
                repeat = false,
                paymentMethod = null,
                currency = CurrencyType.EUR,
                limitAmount = 200.0,
                spentAmount = 150.0,
                projectedAmount = 150.0,
                dailyAverage = 10.0
            )

            val subcategory = Subcategory(
                id = 1,
                categoryId = 1,
                name = "Water",
                icon = IconSubcategory.WATER.icon
            )

            val category = Category(
                id = 1,
                name = "Home",
                icon = IconCategory.HOME.icon,
                color = "#FF5722",
                type = TransactionType.EXPENSE
            )

            val model = BudgetUiModel(
                budget = budget,
                category = category,
                subcategory = subcategory
            )

            BudgetDetailedItem(
                config = BudgetItemConfig(
                    model = model,
                    onClick = {}
                )
            )
        }
    }
}