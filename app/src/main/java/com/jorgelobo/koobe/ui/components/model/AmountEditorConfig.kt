package com.jorgelobo.koobe.ui.components.model

import androidx.compose.ui.graphics.vector.ImageVector

data class AmountEditorConfig(
    val paymentIcon: ImageVector,
    val currencySymbol: String,
    val currencyCode: String,
    val value: Double,
    val onResetClick: () -> Unit,
    val onPaymentSelectorClick: () -> Unit,
    val onCurrencySelectorClick: () -> Unit,
    val onKeyClick: (KeypadKey) -> Unit
)