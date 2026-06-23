package com.jorgelobo.koobe.ui.screen.shortcuts.editor.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.inputs.fields.SelectorIcon
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorUiState
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun ShortcutCategorySection(
    state: ShortcutEditorUiState,
    onIconSelectorClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        Avatar(
            type = AvatarType.EXTRA_LARGE,
            icon = state.icon,
            color = state.category.resolvedColor(),
            isSelected = false
        )

        Spacer(modifier = Modifier.weight(1f))

        SelectorIcon(
            onClick = onIconSelectorClick,
            icon = state.icon,
            iconTint = AppTheme.colors.iconColors.iconPrimary
        )
    }
}