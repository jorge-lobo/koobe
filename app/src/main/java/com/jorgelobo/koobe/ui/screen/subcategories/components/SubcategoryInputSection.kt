package com.jorgelobo.koobe.ui.screen.subcategories.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.avatar.Avatar
import com.jorgelobo.koobe.ui.components.base.inputs.fields.AppInputText
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputFieldConfig
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputSelector
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputSelectorConfig
import com.jorgelobo.koobe.ui.components.base.inputs.fields.SelectorIcon
import com.jorgelobo.koobe.ui.components.model.enums.AvatarType
import com.jorgelobo.koobe.ui.mappers.localizedName
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorConfig
import com.jorgelobo.koobe.ui.screen.subcategories.SubcategoryEditorUiState
import com.jorgelobo.koobe.ui.theme.AppTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing
import com.jorgelobo.koobe.utils.resolvedColor

@Composable
fun SubcategoryInputSection(
    state: SubcategoryEditorUiState,
    config: SubcategoryEditorConfig,
    onNameChanged: (String) -> Unit,
    onIconSelectorClick: () -> Unit,
    onCategoryChangeClick: () -> Unit,
    onResetNameClick: () -> Unit
) {
    val isEditMode = config.isEditMode

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
                icon = state.subcategory.icon,
                color = state.category.resolvedColor(),
                isSelected = false
            )

            Spacer(modifier = Modifier.weight(1f))

            SelectorIcon(
                onClick = onIconSelectorClick,
                icon = state.subcategory.icon,
                iconTint = AppTheme.colors.iconColors.iconPrimary
            )
        }

        AppInputText(
            config = InputFieldConfig(
                value = state.subcategory.localizedName(),
                label = stringResource(R.string.label_name),
                placeholder = stringResource(R.string.input_hint_name_subcategory),
                state = state.nameInputState,
                onValueChange = onNameChanged,
                onResetClick = onResetNameClick
            )
        )

        if (isEditMode) {
            InputSelector(
                config = InputSelectorConfig(
                    onClick = onCategoryChangeClick,
                    value = state.category.localizedName(),
                    label = stringResource(R.string.label_parent_category)
                )
            )
        }
    }
}