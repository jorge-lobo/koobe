package com.jorgelobo.koobe.ui.screen.transactions.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.ui.components.base.inputs.fields.AppInputText
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputFieldConfig
import com.jorgelobo.koobe.ui.mappers.asText
import com.jorgelobo.koobe.ui.screen.transactions.TransactionEditorUiState

/**
 * Input field for the transaction description with reset button.
 */
@Composable
fun TransactionDescriptionSection(
    state: TransactionEditorUiState,
    onDescriptionChange: (String) -> Unit,
    onResetDescriptionClick: () -> Unit
) {
    AppInputText(
        config = InputFieldConfig(
            value = state.descriptionSource.asText(),
            label = stringResource(R.string.label_description),
            placeholder = stringResource(R.string.input_hint_description),
            state = state.inputState,
            onValueChange = onDescriptionChange,
            onResetClick = onResetDescriptionClick
        )
    )
}