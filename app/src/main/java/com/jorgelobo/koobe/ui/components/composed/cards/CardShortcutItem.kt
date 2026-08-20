package com.jorgelobo.koobe.ui.components.composed.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.ui.components.base.chips.AppChip
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun CardShortcutItem(
    modifier: Modifier = Modifier,
    config: CardShortcutItemConfig,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onChipClick: () -> Unit
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
        onDeleteClick = onDeleteClick,
        supportingContent = {
            if (shortcut.repeat && shortcut.period != null) {
                AppChip(
                    text = stringResource(shortcut.period.toRecurrenceLabel()),
                    onClick = onChipClick
                )
            }
        }
    )
}