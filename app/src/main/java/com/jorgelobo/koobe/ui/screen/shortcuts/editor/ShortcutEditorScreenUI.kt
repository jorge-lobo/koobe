package com.jorgelobo.koobe.ui.screen.shortcuts.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.ThemeOption
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.common.modifiers.clearFocusOnTap
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.components.model.enums.InputState
import com.jorgelobo.koobe.ui.components.model.icons.IconPack
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.components.ShortcutAmountSection
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.components.ShortcutCategorySection
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.components.ShortcutNameSection
import com.jorgelobo.koobe.ui.screen.shortcuts.editor.components.ShortcutPeriodSection
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import com.jorgelobo.koobe.ui.theme.dimens.Spacing

@Composable
fun ShortcutEditorScreenUI(
    state: ShortcutEditorUiState,
    modifier: Modifier = Modifier,
    onIconSelectorClick: () -> Unit,
    onNameChanged: (String) -> Unit,
    onResetNameClick: () -> Unit,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onPeriodSelectorClick: () -> Unit,
    onResetAmountClick: () -> Unit,
    onPaymentSelectorClick: () -> Unit,
    onCurrencySelectorClick: () -> Unit,
    onKeyClick: (KeypadKey) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clearFocusOnTap()
            .padding(horizontal = Spacing.Medium, vertical = Spacing.MediumLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Spacing.Small))

        ShortcutCategorySection(
            state = state,
            onIconSelectorClick = onIconSelectorClick
        )

        Spacer(modifier = Modifier.height(Spacing.Large))

        ShortcutNameSection(
            state = state,
            onNameChanged = onNameChanged,
            onResetNameClick = onResetNameClick
        )

        Spacer(modifier = Modifier.height(Spacing.Large))

        ShortcutPeriodSection(
            state = state,
            isChecked = isChecked,
            onCheckedChange = onCheckedChange,
            onPeriodSelectorClick = onPeriodSelectorClick
        )

        Spacer(modifier = Modifier.weight(1f))

        ShortcutAmountSection(
            state = state,
            onResetAmountClick = onResetAmountClick,
            onPaymentSelectorClick = onPaymentSelectorClick,
            onCurrencySelectorClick = onCurrencySelectorClick,
            onKeyClick = onKeyClick
        )
    }
}

@Preview(apiLevel = 34, showBackground = true)
@Composable
fun PreviewShortcutEditorScreen() {
    KoobeTheme(
        themeOption = ThemeOption.LIGHT
    ) {
        val category = Category(
            8,
            "Entertainment",
            IconPack.ENTERTAINMENT,
            "#FFD54F",
            TransactionType.EXPENSE
        )

        ShortcutEditorScreenUI(
            state = ShortcutEditorUiState(
                category = category,
                inputState = InputState.DEFAULT,
                paymentMethodType = PaymentMethodType.CASH,
                currencyType = CurrencyType.EUR,
                name = "name",
                icon = IconPack.CALENDAR,
                amountInput = "10",
                amount = 10.0,
                repeat = false,
                repeatFrequency = PeriodType.MONTHLY,
                shortcutInitialSnapshot = ShortcutInitialSnapshot(
                    category = category,
                    name = "name",
                    icon = IconPack.CALENDAR,
                    amount = 10.0,
                    repeat = false,
                    repeatFrequency = PeriodType.MONTHLY,
                    paymentMethodType = PaymentMethodType.CASH,
                    currencyType = CurrencyType.EUR
                ),
                discardDialog = ConfirmationDialogState(visible = false),
                deleteDialog = ConfirmationDialogState(visible = false),
                iconSelectDialog = SelectorDialogState(
                    visible = false,
                    selected = IconPack.CALENDAR
                ),
                currencyDialog = SelectorDialogState(visible = false, selected = CurrencyType.EUR),
                paymentMethodSelector = SelectorSheetState(
                    visible = false,
                    selected = PaymentMethodType.CASH
                ),
                periodSelector = SelectorSheetState(visible = false, selected = PeriodType.MONTHLY)
            ),
            onIconSelectorClick = {},
            onNameChanged = {},
            onResetNameClick = {},
            isChecked = true,
            onCheckedChange = {},
            onPeriodSelectorClick = {},
            onResetAmountClick = {},
            onPaymentSelectorClick = {},
            onCurrencySelectorClick = {},
            onKeyClick = {}
        )
    }
}