package com.jorgelobo.koobe.ui.components.composed.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun CardShortcutItem(
    modifier: Modifier = Modifier,
    config: CardShortcutItemConfig,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val shortcut = config.shortcut
    val category = config.category

    CardManagementItem(
        modifier = modifier,
        config = CardManagementItemConfig(
            title = shortcut.name,
            icon = shortcut.icon,
            color = category.resolvedColor()
        ),
        onEditClick = onEditClick,
        onDeleteClick = onDeleteClick
    )
}