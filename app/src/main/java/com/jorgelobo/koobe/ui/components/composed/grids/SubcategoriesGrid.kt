package com.jorgelobo.koobe.ui.components.composed.grids

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.grid.GridItemUiModel
import com.jorgelobo.koobe.ui.components.model.icons.IconCategory
import com.jorgelobo.koobe.ui.components.model.icons.IconSubcategory
import com.jorgelobo.koobe.ui.components.model.subcategory.SubcategoryUiModel
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun SubcategoriesGrid(
    modifier: Modifier = Modifier,
    config: SubcategoriesGridConfig
) {
    SelectableGridSection(
        modifier = modifier,
        title = stringResource(R.string.title_subcategory_selector),
        items = config.list.map {
            GridItemUiModel(
                id = it.subcategory.id,
                name = it.subcategory.name,
                icon = it.subcategory.icon,
                color = it.category.resolvedColor()
            )
        },
        selectedId = config.selectedSubcategoryId,
        onItemClick = config.onSubcategoryClick
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewSubcategoriesGrid() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            var selectedSubcategoryId by remember { mutableStateOf<Int?>(null) }

            val mockSubcategories = listOf(
                Subcategory(
                    id = 1,
                    categoryId = 1,
                    name = "Rent",
                    icon = IconSubcategory.RENT.icon
                ),
                Subcategory(
                    id = 2,
                    categoryId = 1,
                    name = "Internet & TV",
                    icon = IconSubcategory.INTERNET_TV.icon
                ),
                Subcategory(
                    id = 3,
                    categoryId = 1,
                    name = "Electricity",
                    icon = IconSubcategory.ELECTRICITY.icon
                ),
                Subcategory(
                    id = 4,
                    categoryId = 1,
                    name = "Appliances",
                    icon = IconSubcategory.APPLIANCES.icon
                ),
                Subcategory(
                    id = 5,
                    categoryId = 1,
                    name = "Water",
                    icon = IconSubcategory.WATER.icon
                ),
                Subcategory(
                    id = 6,
                    categoryId = 1,
                    name = "Gas",
                    icon = IconSubcategory.GAS.icon
                ),
                Subcategory(
                    id = 7,
                    categoryId = 1,
                    name = "Maintenance",
                    icon = IconSubcategory.HOUSE_MAINTENANCE.icon
                ),
                Subcategory(
                    id = 8,
                    categoryId = 1,
                    name = "Condominium",
                    icon = IconSubcategory.CONDOMINIUM.icon
                ),
                Subcategory(
                    id = 9,
                    categoryId = 1,
                    name = "Mortgage",
                    icon = IconSubcategory.MORTGAGE.icon
                ),
                Subcategory(
                    id = 10,
                    categoryId = 1,
                    name = "Furniture",
                    icon = IconSubcategory.FURNITURE.icon
                )
            )

            val category =
                Category(1, "Home", IconCategory.HOME.icon, "#FFB74D", TransactionType.EXPENSE)

            SubcategoriesGrid(
                config = SubcategoriesGridConfig(
                    list = mockSubcategories.map { SubcategoryUiModel(it, category) },
                    selectedSubcategoryId = selectedSubcategoryId,
                    onSubcategoryClick = { selectedSubcategoryId = it }
                )
            )
        }
    }
}