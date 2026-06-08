package com.jorgelobo.koobe.ui.screen.shortcuts.editor.components

import androidx.compose.runtime.Composable
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.components.composed.amount.AmountEditor
import com.jorgelobo.koobe.ui.components.composed.amount.AmountEditorConfig
import com.jorgelobo.koobe.ui.mappers.toIcon
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.ShortcutEditorUiState

@Composable
fun ShortcutAmountSection(
    state: ShortcutEditorUiState,
    onResetAmountClick: () -> Unit,
    onPaymentSelectorClick: () -> Unit,
    onCurrencySelectorClick: () -> Unit,
    onKeyClick: (KeypadKey) -> Unit
) {
    AmountEditor(
        config = AmountEditorConfig(
            paymentIcon = state.paymentMethodType.toIcon(),
            currencyType = state.currencyType,
            value = state.amount,
            onResetClick = onResetAmountClick,
            onPaymentSelectorClick = onPaymentSelectorClick,
            onCurrencySelectorClick = onCurrencySelectorClick,
            onKeyClick = onKeyClick
        )
    )
}