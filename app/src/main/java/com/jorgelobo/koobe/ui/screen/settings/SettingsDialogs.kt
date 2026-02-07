package com.jorgelobo.koobe.ui.screen.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.ui.components.composed.dialogs.OptionSelectorDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.OptionSelectorDialogConfig
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.StartOfWeek
import com.jorgelobo.koobe.ui.components.model.enums.OptionSelectorType

@Composable
fun SettingsDialogs(
    state: SettingsUiState,
    onLanguageSelectorAction: (SelectorDialogAction<AppLanguage>) -> Unit,
    onCurrencySelectorAction: (SelectorDialogAction<CurrencyType>) -> Unit,
    onStartOfWeekSelectorAction: (SelectorDialogAction<StartOfWeek>) -> Unit,
    onPaymentMethodSelectorAction: (SelectorDialogAction<PaymentMethodType>) -> Unit
) {
    if (state.languageDialog.visible) {
        OptionSelectorDialog(
            config = OptionSelectorDialogConfig(
                type = OptionSelectorType.LANGUAGE,
                title = stringResource(R.string.dialog_headline_language_selector),
                selectedLanguage = state.languageSelected,
                onConfirm = { onLanguageSelectorAction(SelectorDialogAction.Apply) },
                onCancel = { onLanguageSelectorAction(SelectorDialogAction.Cancel) },
                onLanguageSelected = { onLanguageSelectorAction(SelectorDialogAction.Select(it)) }
            )
        )
    }

    if (state.currencyDialog.visible) {
        OptionSelectorDialog(
            config = OptionSelectorDialogConfig(
                type = OptionSelectorType.CURRENCY,
                title = stringResource(R.string.dialog_headline_currency_selector),
                selectedCurrency = state.currencySelected,
                onConfirm = { onCurrencySelectorAction(SelectorDialogAction.Apply) },
                onCancel = { onCurrencySelectorAction(SelectorDialogAction.Cancel) },
                onCurrencySelected = { onCurrencySelectorAction(SelectorDialogAction.Select(it)) }
            )
        )
    }

    if (state.startOfWeekDialog.visible) {
        OptionSelectorDialog(
            config = OptionSelectorDialogConfig(
                type = OptionSelectorType.FIRST_WEEKDAY,
                title = stringResource(R.string.dialog_headline_first_day_selector),
                selectedWeekday = state.startOfWeekSelected,
                onConfirm = { onStartOfWeekSelectorAction(SelectorDialogAction.Apply) },
                onCancel = { onStartOfWeekSelectorAction(SelectorDialogAction.Cancel) },
                onWeekdaySelected = { onStartOfWeekSelectorAction(SelectorDialogAction.Select(it)) }
            )
        )
    }

    if (state.paymentMethodDialog.visible) {
        OptionSelectorDialog(
            config = OptionSelectorDialogConfig(
                type = OptionSelectorType.PAYMENT_METHOD,
                title = stringResource(R.string.dialog_headline_payment_method_selector),
                selectedPaymentMethod = state.paymentMethodSelected,
                onConfirm = { onPaymentMethodSelectorAction(SelectorDialogAction.Apply) },
                onCancel = { onPaymentMethodSelectorAction(SelectorDialogAction.Cancel) },
                onPaymentMethodSelected = {
                    onPaymentMethodSelectorAction(
                        SelectorDialogAction.Select(
                            it
                        )
                    )
                }
            )
        )
    }
}