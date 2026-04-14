package com.jorgelobo.koobe.ui.components.base.grid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BaseGridItem(
    modifier: Modifier = Modifier,
    avatarType: AvatarType,
    color: Color,
    icon: IconPack,
    name: String,
    textStyle: TextStyle,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors.textColors

    Column(
        modifier = modifier.clickable(true, onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(Spacing.Small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Avatar(
            type = avatarType,
            icon = icon,
            color = color,
            isSelected = isSelected
        )

        Text(
            text = name,
            style = textStyle,
            color = colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}