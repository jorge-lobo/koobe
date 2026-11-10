package com.jorgelobo.koobe.ui.screen.budgets.editor.components

import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.ui.components.composed.summary.CategorySummary
import com.jorgelobo.koobe.ui.components.composed.summary.CategorySummaryConfig
import com.jorgelobo.koobe.ui.mappers.localizedName
import com.jorgelobo.koobe.ui.screen.budgets.editor.BudgetEditorUiState
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun BudgetEditorCategorySection(
    state: BudgetEditorUiState,
    onChangeClick: () -> Unit
) {
    CategorySummary(
        config = CategorySummaryConfig(
            icon = state.subcategory.icon,
            color = state.category.resolvedColor(),
            primaryName = state.subcategory.localizedName(),
            secondaryName = state.category.localizedName(),
            onChangeClick = onChangeClick
        )
    )
}