package com.jorgelobo.koobe.ui.screen.budgets.manager.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.composed.cards.CardPeriodicBudgetsConfig
import com.jorgelobo.koobe.ui.components.composed.cards.CardPeriodicBudgetsItem
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.screen.budgets.manager.model.PeriodicBudgetsUiModel
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BudgetManagerListSection(
    modifier: Modifier = Modifier,
    periodicBudgets: List<PeriodicBudgetsUiModel>,
    onExpandToggle: (PeriodType) -> Unit,
    onBudgetClick: (BudgetUiModel) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        items(
            items = periodicBudgets,
            key = { it.periodType }
        ) { model ->
            CardPeriodicBudgetsItem(
                config = CardPeriodicBudgetsConfig(
                    model = model,
                    onExpandToggle = { onExpandToggle(model.periodType) },
                    onItemClick = onBudgetClick
                )
            )
        }
    }
}