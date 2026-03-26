package com.jorgelobo.koobe.ui.components.composed.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.base.buttons.types.ButtonEditItem
import com.jorgelobo.koobe.ui.components.composed.base.BaseExpandableCard
import com.jorgelobo.koobe.ui.components.composed.lists.ListSubcategoryItem
import com.jorgelobo.koobe.ui.components.composed.lists.ListSubcategoryItemConfig
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.icons.IconGeneral
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.mappers.localizedName
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun CardCategoryItem(
    modifier: Modifier = Modifier,
    config: CardCategoryItemConfig,
    onEditCategory: (categoryId: Int) -> Unit,
    onEditSubcategory: (subcategoryId: Int) -> Unit,
    onDeleteSubcategory: (subcategoryId: Int) -> Unit,
    onAddSubcategoryClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text
    val category = config.category

    BaseExpandableCard(
        modifier = modifier,
        isExpanded = config.isExpanded,
        onExpandedChange = { config.onCategoryExpandToggle() },
        headerContent = {
            Avatar(
                type = AvatarType.MEDIUM,
                icon = category.icon,
                color = category.resolvedColor(),
                isSelected = false
            )

            Text(
                text = category.localizedName(),
                style = typography.titleMedium,
                color = colors.textColors.textPrimary,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = Spacing.Small)
            )
        },
        headerActions = {
            ButtonEditItem(onClick = { onEditCategory(category.id) })
        },
        expandedContent = {
            category.subcategories.forEach { subcategory ->
                Box(
                    modifier = Modifier.padding(horizontal = Spacing.Small)
                ) {
                    ListSubcategoryItem(
                        config = ListSubcategoryItemConfig(
                            subcategory = subcategory,
                            category = category
                        ),
                        onEditClick = { onEditSubcategory(subcategory.id) },
                        onDeleteClick = { onDeleteSubcategory(subcategory.id) }
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
                        icon = IconGeneral.ADD,
                        onClick = onAddSubcategoryClick
                    )
                )
            }
        }
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewCardCategoryItem() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            val subcategory1 = Subcategory(
                id = 1,
                categoryId = 1,
                name = "Water",
                icon = IconPack.WATER.icon
            )

            val subcategory2 = Subcategory(
                id = 2,
                categoryId = 1,
                name = "Internet & TV",
                icon = IconPack.INTERNET_TV.icon
            )

            val subcategory3 = Subcategory(
                id = 3,
                categoryId = 1,
                name = "Appliances",
                icon = IconPack.APPLIANCES.icon
            )

            val category = Category(
                id = 1,
                name = "Home",
                icon = IconPack.HOME.icon,
                color = "#FF5722",
                type = TransactionType.EXPENSE,
                subcategories = listOf(subcategory1, subcategory2, subcategory3)
            )

            CardCategoryItem(
                config = CardCategoryItemConfig(
                    category = category,
                    isExpanded = true,
                    onCategoryExpandToggle = {}
                ),
                onEditCategory = {},
                onEditSubcategory = {},
                onDeleteSubcategory = {},
                onAddSubcategoryClick = {}
            )
        }
    }
}