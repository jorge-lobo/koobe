package com.jorgelobo.koobe.ui.components.composed.budgets

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.ui.components.model.budget.BudgetUiModel

@Stable
data class BudgetItemConfig(
    val model: BudgetUiModel,
    val currencyType: CurrencyType,
    val onClick: (() -> Unit)? = null
)