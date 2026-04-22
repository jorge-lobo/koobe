package com.jorgelobo.koobe.ui.screen.categories.editor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.composed.cards.CardSubcategoryItem
import com.jorgelobo.koobe.ui.components.composed.cards.CardSubcategoryItemConfig
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorUiState
import com.jorgelobo.koobe.ui.screen.categories.editor.SubcategoryItemUi
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun CategoryEditorSubcategoriesSection(
    state: CategoryEditorUiState,
    subcategories: List<SubcategoryItemUi>,
    onEditSubcategory: (subcategoryId: Int) -> Unit,
    onDeleteSubcategory: (subcategoryId: Int) -> Unit,
    onAddSubcategoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        Text(
            text = stringResource(R.string.title_subcategories),
            style = AppTheme.typography.text.bodyLarge,
            color = AppTheme.colors.textColors.textSecondary
        )

        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            items(
                items = subcategories,
                key = { it.subcategory.id }
            ) { item ->
                CardSubcategoryItem(
                    config = CardSubcategoryItemConfig(
                        subcategory = item.subcategory,
                        category = state.category
                    ),
                    onEditClick = { onEditSubcategory(item.subcategory.id) },
                    onDeleteClick = { onDeleteSubcategory(item.subcategory.id) }
                )
            }
        }

        Row(
            modifier = Modifier.padding(start = Spacing.Medium, bottom = Spacing.Small)
        ) {
            AppButton(
                ButtonConfig(
                    text = stringResource(R.string.btn_add_subcategory),
                    type = ButtonType.TEXT,
                    icon = IconPack.ADD,
                    onClick = onAddSubcategoryClick
                )
            )
        }
    }
}