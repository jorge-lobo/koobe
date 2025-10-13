package com.jorgelobo.koobe.ui.components.composed.amount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.ui.components.base.Background
import com.jorgelobo.koobe.ui.components.base.inputs.InputAmount
import com.jorgelobo.koobe.ui.components.base.inputs.SelectorCurrency
import com.jorgelobo.koobe.ui.components.base.inputs.SelectorPayment
import com.jorgelobo.koobe.ui.components.base.numericKeypad.NumericKeypad
import com.jorgelobo.koobe.ui.components.model.AmountEditorConfig
import com.jorgelobo.koobe.ui.components.model.BackgroundType
import com.jorgelobo.koobe.ui.components.model.InputAmountConfig
import com.jorgelobo.koobe.ui.components.model.icons.IconPayment
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun AmountEditor(
    modifier: Modifier = Modifier,
    config: AmountEditorConfig
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Large)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            SelectorPayment(
                onClick = config.onPaymentSelectorClick,
                icon = config.paymentIcon
            )

            SelectorCurrency(
                onClick = config.onCurrencySelectorClick,
                value = config.currencyCode
            )

            InputAmount(
                config = InputAmountConfig(
                    value = config.value,
                    currencySymbol = config.currencySymbol,
                    onResetClick = config.onResetClick,
                )
            )
        }

        NumericKeypad(
            onKeyClick = config.onKeyClick
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewAmountEditor() {
    KoobeTheme {
        Background(BackgroundType.SCREEN)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            AmountEditor(
                config = AmountEditorConfig(
                    paymentIcon = IconPayment.CASH.icon,
                    currencySymbol = "€",
                    currencyCode = "EUR",
                    value = 25.00,
                    onResetClick = {},
                    onPaymentSelectorClick = {},
                    onCurrencySelectorClick = {},
                    onKeyClick = {}
                )
            )
        }
    }
}