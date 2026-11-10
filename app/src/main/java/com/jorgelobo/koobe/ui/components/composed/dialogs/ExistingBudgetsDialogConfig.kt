package com.jorgelobo.koobe.ui.components.composed.dialogs

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel

@Stable
data class ExistingBudgetsDialogConfig(
    val items: List<BudgetUiModel> = emptyList(),
    val currencyType: CurrencyType,
    val onBudgetClick: (BudgetUiModel) -> Unit,
    val onContinue: () -> Unit,
    val onCancel: () -> Unit
)