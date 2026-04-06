package com.jorgelobo.koobe.ui.components.composed.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.category.Subcategory
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.background.Background
import com.jorgelobo.koobe.ui.components.composed.buttons.EditDeleteActions
import com.jorgelobo.koobe.ui.components.composed.buttons.EditDeleteActionsConfig
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.enums.BackgroundType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens
import com.jorgelobo.koobe.ui.theme.dimens.ListItemSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun CardSubcategoryItem(
    modifier: Modifier = Modifier,
    config: CardSubcategoryItemConfig,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text
    val shape = AppTheme.shapes.medium
    val subcategory = config.subcategory
    val category = config.category

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(ListItemSize.MainHeight)
            .background(colors.containerColors.containerPrimary, shape)
            .border(BorderDimens.Base, colors.containerColors.containerOutline, shape)
            .padding(horizontal = Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Avatar(
            type = AvatarType.MEDIUM,
            icon = subcategory.icon,
            color = category.resolvedColor(),
            isSelected = false
        )

        Text(
            text = subcategory.name,
            style = typography.titleMedium,
            color = colors.textColors.textPrimary,
            modifier = Modifier
                .weight(1f)
                .padding(start = Spacing.Small)
        )

        EditDeleteActions(
            config = EditDeleteActionsConfig(
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        )
    }
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