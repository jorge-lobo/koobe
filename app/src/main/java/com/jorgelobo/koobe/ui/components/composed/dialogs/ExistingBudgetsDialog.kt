package com.jorgelobo.koobe.ui.components.composed.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.dialogs.BaseDialog
import com.jorgelobo.koobe.ui.components.composed.budgets.BudgetItemConfig
import com.jorgelobo.koobe.ui.components.composed.budgets.ExistingBudgetItem
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.color.AccentCoral
import com.jorgelobo.koobe.ui.theme.dimens.DialogSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun ExistingBudgetsDialog(
    modifier: Modifier = Modifier,
    config: ExistingBudgetsDialogConfig
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text

    val height = when (config.items.size) {
        1 -> DialogSize.ExistingBudgets.DialogHeightOneItem
        2 -> DialogSize.ExistingBudgets.DialogHeightTwoItems
        3 -> DialogSize.ExistingBudgets.DialogHeightThreeItems
        else -> DialogSize.ExistingBudgets.DialogHeightFourItems
    }

    BaseDialog(
        modifier = modifier,
        height = height,
        confirmText = stringResource(R.string.btn_continue),
        enable = true,
        onDismissRequest = config.onCancel,
        onConfirm = config.onContinue,
        onCancel = config.onCancel
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.MediumLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = IconPack.WARNING.icon,
                contentDescription = null,
                tint = AccentCoral
            )

            Text(
                text = stringResource(R.string.dialog_headline_existing_budgets),
                style = typography.titleLarge,
                color = colors.textColors.textSecondary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(Spacing.Medium))

        Text(
            text = stringResource(R.string.dialog_message_existing_budgets),
            style = typography.bodyLarge,
            color = colors.textColors.textPrimary
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = DialogSize.ExistingBudgets.ContentHeightMax),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            if (config.items.isEmpty()) return@LazyColumn

            items(
                items = config.items,
                key = { it.budget.id },
            ) { budget ->
                ExistingBudgetItem(
                    config = BudgetItemConfig(
                        model = budget,
                        currencyType = config.currencyType,
                        onClick = { config.onBudgetClick(budget) }
                    )
                )
            }
        }
    }
}