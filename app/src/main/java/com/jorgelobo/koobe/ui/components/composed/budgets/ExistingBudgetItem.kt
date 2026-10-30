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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.buttons.types.ButtonEditItem
import com.jorgelobo.koobe.ui.components.common.MoneyText
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.AvatarSize
import com.jorgelobo.koobe.ui.theme.dimens.BudgetItemSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor
import com.jorgelobo.koobe.R

@Composable
fun ExistingBudgetItem(
    modifier: Modifier = Modifier,
    config: BudgetItemConfig
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    val categoryColor = config.model.category.resolvedColor()
    val currencyType = config.currencyType
    val budget = config.model.budget

    val subcategory = requireNotNull(config.model.subcategory) {
        "ExistingBudgetItem requires a subcategory"
    }

    val recurrenceRes = stringResource(
        if (budget.repeat) R.string.existing_budgets_item_recurring
        else R.string.existing_budgets_item_non_recurring
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(BudgetItemSize.BudgetSimple)
            .padding(end = Spacing.Medium)
            .clickable(enabled = config.onClick != null) { config.onClick?.invoke() },
        horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(
            type = AvatarType.LARGE,
            icon = subcategory.icon,
            color = categoryColor,
            isSelected = false
        )

        Column(
            modifier = Modifier.height(AvatarSize.Large),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = subcategory.name,
                style = typography.text.titleSmall,
                color = colors.textColors.textPrimary
            )

            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                MoneyText(
                    amount = budget.limitAmount,
                    currencyType = currencyType,
                    wholeFontSize = typography.numbers.labelMedium.fontSize,
                    decimalFontSize = typography.numbers.labelSmall.fontSize,
                    textColor = colors.textColors.textSecondary,
                    textAlign = TextAlign.Start,
                    isEnabled = true
                )

                Text(
                    text = "-",
                    style = typography.numbers.labelMedium,
                    color = colors.textColors.textSecondary
                )

                Text(
                    text = stringResource(budget.period.toRecurrenceLabel()),
                    style = typography.text.labelMedium,
                    color = colors.textColors.textPrimary
                )

                Text(
                    text = "-",
                    style = typography.numbers.labelMedium,
                    color = colors.textColors.textSecondary
                )

                Text(
                    text = recurrenceRes,
                    style = typography.text.labelMedium,
                    color = colors.textColors.textPrimary
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        ButtonEditItem(onClick = { config.onClick?.invoke() })
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewExistingBudgetItem() {
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
            val budget = Budget(
                id = 1,
                categoryId = 1,
                subcategoryId = 1,
                period = PeriodType.MONTHLY,
                repeat = false,
                limitAmount = 200.0,
                spentAmount = 150.0,
                projectedAmount = 190.0,
                dailyAverage = 0.0
            )

            val subcategory = Subcategory(
                id = 1,
                categoryId = 1,
                name = "Water",
                icon = IconPack.WATER
            )

            val category = Category(
                id = 1,
                name = "Home",
                icon = IconPack.HOME,
                color = "#FF5722",
                type = TransactionType.EXPENSE
            )

            val model = BudgetUiModel(
                budget = budget,
                category = category,
                subcategory = subcategory
            )

            ExistingBudgetItem(
                config = BudgetItemConfig(
                    model = model,
                    currencyType = CurrencyType.EUR,
                    onClick = {}
                )
            )
        }
    }
}