package com.jorgelobo.koobe.ui.screen.budgets.manager.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.ui.components.composed.cards.CardPeriodicBudgetsConfig
import com.jorgelobo.koobe.ui.components.composed.cards.CardPeriodicBudgetsItem
import com.jorgelobo.koobe.ui.components.composed.emptyState.EmptyStateContent
import com.jorgelobo.koobe.ui.components.composed.emptyState.EmptyStateContentConfig
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel
import com.jorgelobo.koobe.ui.components.model.enums.EmptyStateIconType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.budgets.manager.model.PeriodicBudgetsUiModel
import com.jorgelobo.koobe.ui.theme.color.LightThemeGrey2
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.theme.AppTheme

@Composable
fun BudgetManagerListSection(
    modifier: Modifier = Modifier,
    isEmpty: Boolean,
    periodicBudgets: List<PeriodicBudgetsUiModel>,
    onExpandToggle: (PeriodType) -> Unit,
    onBudgetClick: (BudgetUiModel) -> Unit,
    onAddBudgetClick: () -> Unit
) {
    if (isEmpty) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Spacing.Giant))

            EmptyStateContent(
                config = EmptyStateContentConfig(
                    message = stringResource(R.string.empty_headline_budgets),
                    icon = IconPack.EMPTY,
                    iconTint = LightThemeGrey2,
                    iconType = EmptyStateIconType.BACKGROUND
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.Medium, vertical = Spacing.Large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = stringResource(R.string.empty_hint_budgets),
                    style = AppTheme.typography.text.bodySmall,
                    color = AppTheme.colors.textColors.textSupportMessage,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(Spacing.Medium))

                AppButton(
                    ButtonConfig(
                        text = stringResource(R.string.btn_add_budget),
                        type = ButtonType.SECONDARY,
                        state = UiState.ENABLED,
                        onClick = onAddBudgetClick
                    )
                )
            }
        }
    } else {
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
}