package com.jorgelobo.koobe.ui.screen.budgets.editor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputAmount
import com.jorgelobo.koobe.ui.components.base.inputs.fields.InputAmountConfig
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.components.composed.numericKeypad.NumericKeypad
import com.jorgelobo.koobe.ui.screen.budgets.editor.BudgetEditorUiState
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun BudgetEditorAmountSection(
    state: BudgetEditorUiState,
    onResetAmountClick: () -> Unit,
    onKeyClick: (KeypadKey) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Large),
        horizontalAlignment = Alignment.End
    ) {
        InputAmount(
            config = InputAmountConfig(
                value = state.limitAmount,
                currencyType = state.currencyType,
                onResetClick = onResetAmountClick,
            )
        )

        NumericKeypad(
            onKeyClick = onKeyClick
        )
    }
}