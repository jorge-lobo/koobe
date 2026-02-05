package com.jorgelobo.koobe.ui.components.composed.lists

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
import com.jorgelobo.koobe.ui.theme.dimens.ListItemSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun ListSubcategoryItem(
    modifier: Modifier = Modifier,
    config: ListSubcategoryItemConfig,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val colors = AppTheme.colors.textColors
    val typography = AppTheme.typography.text
    val category = config.category
    val subcategory = config.subcategory

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(ListItemSize.MainHeight)
            .padding(start = Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        Avatar(
            type = AvatarType.SMALL,
            icon = subcategory.icon,
            color = category.resolvedColor(),
            isSelected = false
        )

        Text(
            text = subcategory.name,
            style = typography.titleMedium,
            color = colors.textPrimary,
            modifier = Modifier.weight(1f)
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
                icon = IconPack.HOME.icon,
                color = "#FF5722",
                type = TransactionType.EXPENSE
            )

            val subcategory = Subcategory(
                id = 1,
                categoryId = 1,
                name = "Water",
                icon = IconPack.WATER.icon
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