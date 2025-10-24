package com.jorgelobo.koobe.ui.components.composed.budgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.info.StatusIndicator
import com.jorgelobo.koobe.ui.components.base.progressBar.AppProgressBar
import com.jorgelobo.koobe.ui.components.base.progressBar.ProgressBarConfig
import com.jorgelobo.koobe.ui.components.common.MoneyText
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconCategory
import com.jorgelobo.koobe.ui.components.model.icons.IconSubcategory
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.dimens.AvatarSize
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens
import com.jorgelobo.koobe.ui.theme.dimens.BudgetItemSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolveColor

@Composable
fun BudgetSimpleItem(
    modifier: Modifier = Modifier,
    config: BudgetItemConfig
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    val categoryColor = config.category.resolveColor()
    val spent = config.budget.spentAmount
    val limit = config.budget.limitAmount
    val projected = config.budget.projectedAmount
    val projectedPercentage = if (limit > 0) (projected / limit) else 0.0
    val spentPercentage = if (limit > 0) (spent / limit) else 0.0
    val showWarning = projectedPercentage > 0.8

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(BudgetItemSize.BudgetSimple)
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
                type = AvatarType.LARGE,
                icon = config.subcategory.icon,
                color = categoryColor,
                isSelected = false
            )

            Column(
                modifier = Modifier.height(AvatarSize.Large),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = config.subcategory.name,
                        style = typography.text.titleSmall,
                        color = colors.textColors.textPrimary
                    )

                    if (showWarning) {
                        StatusIndicator(
                            percentage = projectedPercentage
                        )
                    }
                }

                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    MoneyText(
                        amount = spent,
                        currencyType = config.budget.currency,
                        wholeFontSize = typography.numbers.labelMedium.fontSize,
                        decimalFontSize = typography.numbers.labelSmall.fontSize,
                        textColor = if (spent > limit) AccentCoral else colors.textColors.textSecondary,
                        textAlign = TextAlign.End,
                        isEnabled = true
                    )

                    Text(
                        text = "/",
                        style = typography.numbers.labelMedium,
                        color = colors.textColors.textSecondary
                    )

                    MoneyText(
                        amount = limit,
                        currencyType = config.budget.currency,
                        wholeFontSize = typography.numbers.labelMedium.fontSize,
                        decimalFontSize = typography.numbers.labelSmall.fontSize,
                        textColor = colors.textColors.textSecondary,
                        textAlign = TextAlign.End,
                        isEnabled = true
                    )
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

        HorizontalDivider(
            thickness = BorderDimens.Base,
            color = colors.containerColors.divider
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewBudgetSimpleItem() {
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
                projectedAmount = 190.0,
                dailyAverage = 0.0
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

            BudgetSimpleItem(
                config = BudgetItemConfig(
                    budget = budget,
                    category = category,
                    subcategory = subcategory,
                    onClick = {}
                )
            )
        }
    }
}