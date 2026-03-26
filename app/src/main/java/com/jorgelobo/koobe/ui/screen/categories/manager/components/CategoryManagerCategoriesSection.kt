package com.jorgelobo.koobe.ui.screen.categories.manager.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.ui.components.composed.cards.CardCategoryItem
import com.jorgelobo.koobe.ui.components.composed.cards.CardCategoryItemConfig
import com.jorgelobo.koobe.ui.components.composed.emptyState.EmptyStateContent
import com.jorgelobo.koobe.ui.components.composed.emptyState.EmptyStateContentConfig
import com.jorgelobo.koobe.ui.components.model.enums.EmptyStateIconType
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.screen.categories.manager.CategoryItemUi
import com.jorgelobo.koobe.ui.theme.color.LightThemeGrey2
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.R

@Composable
fun CategoryManagerCategoriesSection(
    isEmpty: Boolean,
    categories: List<CategoryItemUi>,
    onCategoryExpandToggle: (Int) -> Unit,
    onEditSubcategory: (categoryId: Int, subcategoryId: Int) -> Unit,
    onDeleteSubcategory: (subcategoryId: Int) -> Unit,
    onAddSubcategoryClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isEmpty) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Spacing.Giant))

            EmptyStateContent(
                config = EmptyStateContentConfig(
                    message = stringResource(R.string.empty_headline_categories),
                    icon = IconGeneral.EMPTY.icon,
                    iconTint = LightThemeGrey2,
                    iconType = EmptyStateIconType.BACKGROUND
                )
            )
        }
    } else {
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
                    onEditSubcategory = { subcategoryId -> onEditSubcategory(item.category.id, subcategoryId) },
                    onDeleteSubcategory = onDeleteSubcategory,
                    onAddSubcategoryClick = { onAddSubcategoryClick(item.category.id) }
                )
            }
        }
    }
}