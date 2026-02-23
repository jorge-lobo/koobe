package com.jorgelobo.koobe.ui.components.composed.cards

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.CategoryHistory
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType

@Stable
data class CardHistoricItemConfig(
    val category: Category,
    val categoryHistory: CategoryHistory,
    val currencyType: CurrencyType,
    val isExpanded: Boolean,
    val expandedSubcategories: Set<Int>,
    val onCategoryExpandToggle: () -> Unit,
    val onSubcategoryExpandToggle: (Int) -> Unit
)