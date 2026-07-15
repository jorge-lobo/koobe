package com.jorgelobo.koobe.ui.components.composed.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.subcategory.Subcategory
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.mappers.localizedName
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun ListSubcategoryItem(
    modifier: Modifier = Modifier,
    config: ListSubcategoryItemConfig,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val category = config.category
    val subcategory = config.subcategory

    ManagementListItem(
        modifier = modifier,
        config = ManagementListItemConfig(
            title = subcategory.localizedName(),
            icon = subcategory.icon,
            color = category.resolvedColor()
        ),
        onEditClick = onEditClick,
        onDeleteClick = onDeleteClick
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewListSubcategoryItem() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium)
        ) {
            val category = Category(
                id = 1,
                name = "Home",
                icon = IconPack.HOME,
                color = "#FF5722",
                type = TransactionType.EXPENSE
            )

            val subcategory = Subcategory(
                id = 1,
                categoryId = 1,
                name = "Water",
                icon = IconPack.WATER
            )

            ListSubcategoryItem(
                config = ListSubcategoryItemConfig(
                    subcategory = subcategory,
                    category = category
                ),
                onEditClick = {},
                onDeleteClick = {}
            )

            ListSubcategoryItem(
                config = ListSubcategoryItemConfig(
                    subcategory = subcategory,
                    category = category
                ),
                onEditClick = {},
                onDeleteClick = {}
            )
        }
    }
}