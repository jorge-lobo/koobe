package com.jorgelobo.koobe.ui.screen.categories.manager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.screen.categories.manager.components.CategoryManagerCategoriesSection
import com.jorgelobo.koobe.ui.screen.categories.manager.components.CategoryManagerToggleSection
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun CategoryManagerScreenUI(
    state: CategoryManagerUiState,
    modifier: Modifier = Modifier,
    onTransactionTypeChange: (TransactionType) -> Unit,
    onCategoryExpandToggle: (Int) -> Unit,
    onEditCategory: (categoryId: Int) -> Unit,
    onEditSubcategory: (categoryId: Int, subcategoryId: Int) -> Unit,
    onDeleteSubcategory: (subcategoryId: Int) -> Unit,
    onAddSubcategoryClick: (Int) -> Unit,
) {

    val categoryItems = remember(state.categories, state.expandedCategoryId) {
        state.categories.map { category ->
            CategoryItemUi(
                category = category,
                isExpanded = category.id == state.expandedCategoryId
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = Spacing.Large),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
    ) {
        CategoryManagerToggleSection(
            transactionTypeSelected = state.transactionTypeSelected,
            onTransactionTypeChange = onTransactionTypeChange
        )

        CategoryManagerCategoriesSection(
            isEmpty = state.categories.isEmpty(),
            modifier = Modifier.weight(1f),
            categories = categoryItems,
            onCategoryExpandToggle = onCategoryExpandToggle,
            onEditCategory = onEditCategory,
            onEditSubcategory = onEditSubcategory,
            onDeleteSubcategory = onDeleteSubcategory,
            onAddSubcategoryClick = onAddSubcategoryClick
        )
    }
}