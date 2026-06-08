package com.jorgelobo.koobe.ui.screen.shortcuts.editor.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.ui.components.base.inputs.fields.AppInputText
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputFieldConfig
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorUiState
import com.jorgelobo.koobe.R

@Composable
fun ShortcutNameSection(
    state: ShortcutEditorUiState,
    onNameChanged: (String) -> Unit,
    onResetNameClick: () -> Unit
) {
    AppInputText(
        config = InputFieldConfig(
            value = state.name,
            label = stringResource(R.string.label_name),
            placeholder = stringResource(R.string.input_hint_name_shortcut),
            state = state.inputState,
            onValueChange = onNameChanged,
            onResetClick = onResetNameClick
        )
    )
}