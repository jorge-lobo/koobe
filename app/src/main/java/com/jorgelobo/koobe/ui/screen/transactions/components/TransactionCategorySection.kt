package com.jorgelobo.koobe.ui.screen.transactions.components

import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.ui.components.composed.summary.CategorySummary
import com.jorgelobo.koobe.ui.components.composed.summary.CategorySummaryConfig
import com.jorgelobo.koobe.ui.mappers.localizedName
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorUiState
import com.jorgelobo.koobe.utils.resolvedColor

/**
 * Displays the selected category, subcategory/shortcut, and allows changing it.
 */
@Composable
fun TransactionCategorySection(
    state: TransactionEditorUiState,
    onChangeClick: () -> Unit
) {
    CategorySummary(
        config = CategorySummaryConfig(
            icon = state.category.icon,
            color = state.category.resolvedColor(),
            categoryName = state.category.localizedName(),
            subcategoryName = state.subcategory?.localizedName() ?: state.shortcut?.name,
            onChangeClick = onChangeClick
        )
    )
}