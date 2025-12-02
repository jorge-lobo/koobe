package com.jorgelobo.koobe.ui.screen.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.ui.components.composed.containers.BudgetsSummaryCard
import com.jorgelobo.koobe.ui.components.composed.containers.BudgetsSummaryCardConfig
import com.jorgelobo.koobe.ui.components.composed.containers.QuickStatsCard
import com.jorgelobo.koobe.ui.components.composed.containers.QuickStatsCardConfig
import com.jorgelobo.koobe.ui.components.composed.containers.ShortcutsSummaryCard
import com.jorgelobo.koobe.ui.components.composed.containers.ShortcutsSummaryCardConfig
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.components.model.shortcut.ShortcutUiModel
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun DashboardCardsSection(
    dailyIncome: Double,
    dailyExpenses: Double,
    weeklyIncome: Double,
    weeklyExpenses: Double,
    currencyType: CurrencyType,
    budgetItems: List<BudgetUiModel>,
    shortcutItems: List<ShortcutUiModel>,
    onBudgetItemClick: (BudgetUiModel) -> Unit,
    onShortcutItemClick: (ShortcutUiModel) -> Unit,
    onBudgetActionClick: () -> Unit,
    onShortcutActionClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        QuickStatsCard(
            config = QuickStatsCardConfig(
                dailyIncome = dailyIncome,
                dailyExpenses = dailyExpenses,
                weeklyIncome = weeklyIncome,
                weeklyExpenses = weeklyExpenses,
                currencyType = currencyType
            )
        )

        BudgetsSummaryCard(
            config = BudgetsSummaryCardConfig(
                items = budgetItems,
                onBudgetClick = onBudgetItemClick,
                onActionClick = onBudgetActionClick
            )
        )

        ShortcutsSummaryCard(
            config = ShortcutsSummaryCardConfig(
                items = shortcutItems,
                onShortcutClick = onShortcutItemClick,
                onActionClick = onShortcutActionClick
            )
        )
    }
}