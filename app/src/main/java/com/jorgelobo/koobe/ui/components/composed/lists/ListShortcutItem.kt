package com.jorgelobo.koobe.ui.components.composed.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun ListShortcutItem(
    modifier: Modifier = Modifier,
    config: ListShortcutItemConfig,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val shortcut = config.shortcut
    val category = config.category

    ManagementListItem(
        modifier = modifier,
        config = ManagementListItemConfig(
            title = shortcut.name,
            icon = shortcut.icon,
            color = category.resolvedColor()
        ),
        onEditClick = onEditClick,
        onDeleteClick = onDeleteClick
    )
}