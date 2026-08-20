package com.jorgelobo.koobe.ui.components.composed.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.transaction.Shortcut
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.mappers.localizedName
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.AvatarSize
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun ShortcutBottomSheetHeader(
    shortcut: Shortcut,
    category: Category
) {
    val typography = AppTheme.typography.text
    val textColors = AppTheme.colors.textColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Medium)
            .height(AvatarSize.Large),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(
            type = AvatarType.LARGE,
            icon = shortcut.icon,
            color = category.resolvedColor()
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = shortcut.name,
                style = typography.titleLarge,
                color = textColors.textPrimary
            )

            Spacer(modifier = Modifier.height(Spacing.Micro))

            Text(
                text = category.localizedName(),
                style = typography.bodyMedium,
                color = textColors.textSupportMessage
            )
        }
    }
}