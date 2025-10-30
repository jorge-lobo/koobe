package com.jorgelobo.koobe.ui.components.composed.cards

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.budget.Budget
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType

@Stable
data class CardPeriodicBudgetsConfig(
    val periodType: PeriodType,
    val currencyType: CurrencyType,
    val budgetsCount: Int,
    val totalLimit: Double,
    val totalSpent: Double,
    val budgets: List<Budget>,
    val categories: List<Category>,
    val subcategories: List<Subcategory>
)