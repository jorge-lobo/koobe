package com.jorgelobo.koobe.ui.components.composed.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.composed.buttons.EditDeleteActions
import com.jorgelobo.koobe.ui.components.composed.buttons.EditDeleteActionsConfig
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.ListItemSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun ManagementListItem(
    modifier: Modifier = Modifier,
    config: ManagementListItemConfig,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val colors = AppTheme.colors.textColors
    val typography = AppTheme.typography.text

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
            icon = config.icon,
            color = config.color,
            isSelected = false
        )

        Text(
            text = config.title,
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