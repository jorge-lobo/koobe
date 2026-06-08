package com.jorgelobo.koobe.ui.screen.shortcuts.editor.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorUiState
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun ShortcutSaveSection(
    state: ShortcutEditorUiState,
    onSaveClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.Medium, vertical = Spacing.MediumLarge)
    ) {
        AppButton(
            ButtonConfig(
                text = stringResource(R.string.btn_save),
                type = ButtonType.PRIMARY,
                state = if (state.isSaveEnabled && !state.isLoading) UiState.ENABLED else UiState.DISABLED,
                onClick = onSaveClick
            )
        )
    }
}