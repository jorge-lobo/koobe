package com.jorgelobo.koobe.ui.screen.categories.selector.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.base.toggles.TransactionToggle
import com.jorgelobo.koobe.ui.components.base.toggles.transactionToggleConfig
import com.jorgelobo.koobe.ui.components.composed.grids.CategoriesGrid
import com.jorgelobo.koobe.ui.components.composed.grids.CategoriesGridConfig
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

/**
 * Composable that provides a category selection interface.
 *
 * It displays a grid of categories and includes options to filter by transaction type
 * and a shortcut to create a new category.
 *
 * @param showToggle Whether to display the [TransactionToggle] for switching between transaction types.
 * @param categories The list of [Category] items to be displayed in the grid.
 * @param transactionSelected The currently active [TransactionType].
 * @param onTransactionTypeChange Callback triggered when the transaction type toggle is changed.
 * @param selectedCategoryId The ID of the currently selected category, or null if none is selected.
 * @param onCategoryIdChange Callback triggered when a category is selected from the grid.
 * @param onCreateCategoryClick Callback triggered when the "create category" button is clicked.
 */
@Composable
fun CategorySelection(
    showToggle: Boolean,
    categories: List<Category>,
    transactionSelected: TransactionType,
    onTransactionTypeChange: (TransactionType) -> Unit,
    selectedCategoryId: Int?,
    onCategoryIdChange: (Int) -> Unit,
    onCreateCategoryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.Medium, vertical = Spacing.MediumLarge),
        verticalArrangement = Arrangement.spacedBy(Spacing.MediumLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showToggle) {
            TransactionToggle(
                config = transactionToggleConfig(
                    selected = transactionSelected,
                    onOptionSelected = onTransactionTypeChange
                )
            )
        }

        CategoriesGrid(
            config = CategoriesGridConfig(
                list = categories,
                selectedCategoryId = selectedCategoryId,
                onCategoryClick = onCategoryIdChange,
                onCreateCategoryClick = onCreateCategoryClick
            )
        )
    }
}