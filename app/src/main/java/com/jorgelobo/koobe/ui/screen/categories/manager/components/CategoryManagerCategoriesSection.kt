package com.jorgelobo.koobe.ui.screen.categories.manager.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.ui.components.composed.cards.CardCategoryItem
import com.jorgelobo.koobe.ui.components.composed.cards.CardCategoryItemConfig
import com.jorgelobo.koobe.ui.screen.categories.manager.CategoryItemUi
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun CategoryManagerCategoriesSection(
    categories: List<CategoryItemUi>,
    onCategoryExpandToggle: (Int) -> Unit,
    onEditSubcategory: (Int) -> Unit,
    onDeleteSubcategory: (Int) -> Unit,
    onAddSubcategoryClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        items(
            items = categories,
            key = { it.category.id }
        ) { item ->
            CardCategoryItem(
                config = CardCategoryItemConfig(
                    category = item.category,
                    isExpanded = item.isExpanded,
                    onCategoryExpandToggle = { onCategoryExpandToggle(item.category.id) }
                ),
                onEditSubcategory = onEditSubcategory,
                onDeleteSubcategory = onDeleteSubcategory,
                onAddSubcategoryClick = { onAddSubcategoryClick(item.category.id) }
            )
        }
    }
}