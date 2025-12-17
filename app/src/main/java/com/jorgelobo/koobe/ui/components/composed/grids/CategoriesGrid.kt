package com.jorgelobo.koobe.ui.components.composed.grids

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.base.grid.BaseGridItem
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.mappers.localizedName
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun CategoriesGrid(
    modifier: Modifier = Modifier,
    config: CategoriesGridConfig
) {
    val typography = AppTheme.typography.text

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.MediumLarge),
        verticalArrangement = Arrangement.spacedBy(Spacing.MediumLarge)
    ) {
        items(items = config.list) { category ->
            val isSelected = category.id == config.selectedCategoryId

            BaseGridItem(
                avatarType = AvatarType.EXTRA_LARGE,
                color = category.resolvedColor(),
                icon = category.icon,
                name = category.localizedName(),
                textStyle = typography.bodyMedium,
                isSelected = isSelected,
                onClick = { config.onCategoryClick(category.id) }
            )
        }
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewCategoriesGrid() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            var selectedCategoryId by remember { mutableStateOf<Int?>(null) }

            val mockCategories = listOf(
                Category(1, "Home", IconPack.HOME.icon, "#FFB74D", TransactionType.EXPENSE),
                Category(2, "Grocery", IconPack.GROCERY.icon, "#DAE067", TransactionType.EXPENSE),
                Category(3, "Dining", IconPack.DINING.icon, "#FFA58A", TransactionType.EXPENSE),
                Category(4, "Transportation", IconPack.TRANSPORTATION.icon, "#3EB5A9", TransactionType.EXPENSE),
                Category(5, "Health", IconPack.HEALTH.icon, "#BD5555", TransactionType.EXPENSE),
                Category(6, "Apparel", IconPack.APPAREL.icon, "#BA68C8", TransactionType.EXPENSE),
                Category(7, "Technology", IconPack.TECHNOLOGY.icon, "#6476D1", TransactionType.EXPENSE),
                Category(8, "Entertainment", IconPack.ENTERTAINMENT.icon, "#FFD54F", TransactionType.EXPENSE),
                Category(9, "Education", IconPack.EDUCATION.icon, "#59BD5E", TransactionType.EXPENSE),
                Category(10, "Travel", IconPack.TRAVEL.icon, "#5A9BE0", TransactionType.EXPENSE),
                Category(11, "Essentials", IconPack.ESSENTIALS.icon, "#A1887F", TransactionType.EXPENSE),
                Category(12, "Pets", IconPack.PETS.icon, "#F06292", TransactionType.EXPENSE),
                Category(13, "Body Care", IconPack.BODY_CARE.icon, "#E5A8F0", TransactionType.EXPENSE),
                Category(14, "Sports", IconPack.SPORTS.icon, "#B0E86F", TransactionType.EXPENSE),
                Category(15, "Family", IconPack.FAMILY.icon, "#7DC3E3", TransactionType.EXPENSE),
                Category(16, "Miscellaneous", IconPack.MISCELLANEOUS.icon, "#B0BEC5", TransactionType.EXPENSE),
            )

            CategoriesGrid(
                config = CategoriesGridConfig(
                    list = mockCategories,
                    selectedCategoryId = selectedCategoryId,
                    onCategoryClick = { selectedCategoryId = it }
                )
            )
        }
    }
}