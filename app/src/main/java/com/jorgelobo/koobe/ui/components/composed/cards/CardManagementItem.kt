package com.jorgelobo.koobe.ui.components.composed.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.jorgelobo.koobe.ui.theme.dimens.BorderDimens
import com.jorgelobo.koobe.ui.theme.dimens.ListItemSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun CardManagementItem(
    modifier: Modifier = Modifier,
    config: CardManagementItemConfig,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography.text
    val shape = AppTheme.shapes.medium

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(ListItemSize.MainHeight)
            .background(colors.containerColors.containerPrimary, shape)
            .border(
                BorderDimens.Base,
                colors.containerColors.containerOutline,
                shape
            )
            .padding(horizontal = Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Avatar(
            type = AvatarType.MEDIUM,
            icon = config.icon,
            color = config.color,
            isSelected = false
        )

        Text(
            text = config.title,
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