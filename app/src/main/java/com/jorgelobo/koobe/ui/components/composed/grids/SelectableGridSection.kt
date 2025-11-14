package com.jorgelobo.koobe.ui.components.composed.grids

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.ui.components.base.grid.BaseGridItem
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.grid.GridItemUiModel
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun SelectableGridSection(
    modifier: Modifier = Modifier,
    title: String,
    items: List<GridItemUiModel>,
    selectedId: Int?,
    onItemClick: (Int) -> Unit
) {
    val typography = AppTheme.typography.text

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        Text(
            text = title,
            style = typography.titleMedium,
            color = AppTheme.colors.textColors.textSecondary
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.MediumLarge),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            items(items) { item ->
                val isSelected = item.id == selectedId

                BaseGridItem(
                    avatarType = AvatarType.LARGE,
                    color = item.color,
                    icon = item.icon,
                    name = item.name,
                    textStyle = typography.bodySmall,
                    isSelected = isSelected,
                    onClick = { onItemClick(item.id) }
                )
            }
        }
    }
}