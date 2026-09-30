package com.jorgelobo.koobe.ui.screen.budgets.manager.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.ui.components.composed.containers.BalanceContainer
import com.jorgelobo.koobe.ui.components.composed.containers.BalanceContainerConfig
import com.jorgelobo.koobe.ui.components.model.enums.ScreenType
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BudgetManagerFinancialSection(
    currencyType: CurrencyType,
    balance: Double,
    income: Double,
    expenses: Double
) {
    BalanceContainer(
        config = BalanceContainerConfig(
            balance = balance,
            income = income,
            expenses = expenses,
            currencyType = currencyType,
            screenType = ScreenType.BUDGET_MANAGER
        ),
        modifier = Modifier.padding(horizontal = Spacing.Medium)
    )
}