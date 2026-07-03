package com.jorgelobo.koobe.ui.components.composed.cards

import androidx.compose.foundation.layout.Arrangement
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
fun CardSubcategoryItem(
    modifier: Modifier = Modifier,
    config: CardSubcategoryItemConfig,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    CardManagementItem(
        modifier = modifier,
        config = CardManagementItemConfig(
            title = config.subcategory.localizedName(),
            icon = config.subcategory.icon,
            color = config.category.resolvedColor()
        ),
        onEditClick = onEditClick,
        onDeleteClick = onDeleteClick
    )
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewCardSubcategoryItem() {
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
            val subcategory = Subcategory(
                id = 1,
                categoryId = 1,
                name = "Water",
                icon = IconPack.WATER
            )

            val category = Category(
                id = 1,
                name = "Home",
                icon = IconPack.HOME,
                color = "#FF5722",
                type = TransactionType.EXPENSE,
                subcategories = listOf(subcategory)
            )

            val config = CardSubcategoryItemConfig(
                subcategory = subcategory,
                category = category
            )

            CardSubcategoryItem(
                config = config,
                onEditClick = {},
                onDeleteClick = {}
            )
        }
    }
}