package com.jorgelobo.koobe.ui.screen.categories.editor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.inputs.fields.AppInputText
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputFieldConfig
import com.jorgelobo.koobe.ui.components.base.inputs.fields.SelectorColor
import com.jorgelobo.koobe.ui.components.base.inputs.fields.SelectorIcon
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.screen.categories.editor.CategoryEditorUiState
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.mappers.localizedName

@Composable
fun CategoryEditorInputSection(
    state: CategoryEditorUiState,
    onNameChanged: (String) -> Unit,
    onResetNameClick: () -> Unit,
    onIconSelectorClick: () -> Unit,
    onColorSelectorClick: () -> Unit
) {
    val colors = AppTheme.colors
    val avatarColor = state.category.resolvedColor()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Large)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            Avatar(
                type = AvatarType.EXTRA_LARGE,
                icon = state.category.icon,
                color = avatarColor,
                isSelected = false
            )

            Spacer(modifier = Modifier.weight(1f))

            SelectorIcon(
                onClick = onIconSelectorClick,
                icon = state.category.icon,
                iconTint = colors.iconColors.iconPrimary
            )

            Spacer(modifier = Modifier.width(Spacing.Medium))

            SelectorColor(
                onClick = onColorSelectorClick,
                color = state.category.resolvedColor()
            )
        }

        AppInputText(
            config = InputFieldConfig(
                value = state.category.localizedName(),
                label = stringResource(R.string.label_name),
                placeholder = stringResource(R.string.input_hint_name_category),
                state = state.nameInputState,
                onValueChange = onNameChanged,
                onResetClick = onResetNameClick
            )
        )
    }
}