package com.jorgelobo.koobe.ui.screen.budgets.manager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.budgets.manager.components.BudgetManagerFinancialSection
import com.jorgelobo.koobe.ui.screen.budgets.manager.components.BudgetManagerListSection
import com.jorgelobo.koobe.ui.screen.budgets.manager.model.PeriodicBudgetsUiModel
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BudgetManagerScreenUI(
    state: BudgetManagerUiState,
    modifier: Modifier = Modifier,
    onExpandToggle: (PeriodType) -> Unit,
    onBudgetClick: (BudgetUiModel) -> Unit,
    onAddBudgetClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = Spacing.Large),
        verticalArrangement = Arrangement.spacedBy(Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BudgetManagerFinancialSection(
            currencyType = state.currencyType,
            balance = state.balance,
            income = state.income,
            expenses = state.expenses
        )

        BudgetManagerListSection(
            modifier = Modifier.weight(1f),
            isEmpty = state.periodicBudgets.isEmpty(),
            periodicBudgets = state.periodicBudgets,
            onExpandToggle = onExpandToggle,
            onBudgetClick = onBudgetClick,
            onAddBudgetClick = onAddBudgetClick
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
private fun PreviewBudgetManagerScreenUI() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        BudgetManagerScreenUI(
            state = previewBudgetManagerUiState(),
            onExpandToggle = {},
            onBudgetClick = {},
            onAddBudgetClick = {}
        )
    }
}

fun previewBudgetManagerUiState(): BudgetManagerUiState {

    val budget1 = Budget(
        id = 1,
        categoryId = 1,
        subcategoryId = 1,
        period = PeriodType.WEEKLY,
        repeat = false,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR,
        limitAmount = 70.0,
        spentAmount = 55.0,
        projectedAmount = 64.0,
        dailyAverage = 9.0
    )

    val budget2 = Budget(
        id = 2,
        categoryId = 2,
        subcategoryId = 2,
        period = PeriodType.WEEKLY,
        repeat = false,
        paymentMethod = PaymentMethodType.CASH,
        currency = CurrencyType.EUR,
        limitAmount = 150.0,
        spentAmount = 135.0,
        projectedAmount = 164.0,
        dailyAverage = 23.43
    )

    val homeCategory = Category(
        id = 6,
        name = "Home",
        icon = IconPack.HOME,
        color = "#FFB74D",
        type = TransactionType.EXPENSE,
        subcategories = emptyList()
    )

    val transportCategory = Category(
        id = 4,
        name = "Transportation",
        icon = IconPack.TRANSPORTATION,
        color = "#3EB5A9",
        type = TransactionType.EXPENSE,
        subcategories = emptyList()
    )

    val weeklyBudgets = PeriodicBudgetsUiModel(
        periodType = PeriodType.WEEKLY,
        currencyType = CurrencyType.EUR,
        budgets = listOf(
            BudgetUiModel(
                budget = budget1,
                category = transportCategory,
                subcategory = null
            ),
            BudgetUiModel(
                budget = budget2,
                category = homeCategory,
                subcategory = null
            )
        ),
        totalLimit = 220.0,
        totalSpent = 190.0,
        isExpanded = true
    )

    return BudgetManagerUiState(
        currencyType = CurrencyType.EUR,
        balance = 2822.90,
        income = 3000.00,
        expenses = 177.10,
        periodicBudgets = listOf(weeklyBudgets)
    )
}