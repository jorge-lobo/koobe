package com.jorgelobo.koobe.ui.screen.transactions.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.buttons.base.ButtonConfig
import com.jorgelobo.koobe.ui.components.base.buttons.types.AppButton
import com.jorgelobo.koobe.ui.components.model.enums.ButtonType
import com.jorgelobo.koobe.ui.components.model.enums.UiState
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorUiState

/**
 * Primary save button, enabled or disabled based on state.isSaveButtonEnabled.
 */
@Composable
fun TransactionSaveButton(
    state: TransactionEditorUiState,
    onSaveClick: () -> Unit
) {
    AppButton(
        ButtonConfig(
            text = stringResource(R.string.btn_save),
            type = ButtonType.PRIMARY,
            state = if (state.isSaveButtonEnabled) UiState.ENABLED else UiState.DISABLED,
            onClick = onSaveClick
        )
    )
}