package com.jorgelobo.koobe.ui.screen.transactions

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.ui.components.base.dialogs.AppDatePickerDialog
import com.jorgelobo.koobe.ui.components.base.dialogs.AppDatePickerDialogConfig
import com.jorgelobo.koobe.ui.components.composed.dialogs.DiscardDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.OptionSelectorDialog
import com.jorgelobo.koobe.ui.components.composed.dialogs.OptionSelectorDialogConfig
import com.jorgelobo.koobe.ui.components.composed.sheets.ListSelectorBottomSheet
import com.jorgelobo.koobe.ui.components.composed.sheets.ListSelectorBottomSheetConfig
import com.jorgelobo.koobe.ui.components.model.enums.OptionSelectorType
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction

/**
 * Composable that displays all dialogs and bottom sheets for the transaction editor.
 *
 * This function observes the [TransactionEditorUiState] and conditionally shows:
 * - A discard confirmation dialog
 * - Currency selection dialog
 * - Date picker dialog
 * - Payment method selection bottom sheet
 *
 * All user interactions are propagated back via callback lambdas, which are typically handled
 * by the [TransactionEditorViewModel].
 *
 * @param state The current UI state of the transaction editor.
 * @param sheetState The [SheetState] used for the modal bottom sheet.
 * @param onDialogAction Callback for handling confirmation dialog actions.
 * @param onCurrencySelectorDialogAction Callback for handling currency selection dialog actions.
 * @param onDatePickerDialogAction Callback for handling date picker dialog actions.
 * @param onPaymentSelectorAction Callback for handling payment method selection actions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditorDialogs(
    state: TransactionEditorUiState,
    sheetState: SheetState,
    onDialogAction: (ConfirmationDialogAction) -> Unit,
    onCurrencySelectorDialogAction: (SelectorDialogAction<CurrencyType>) -> Unit,
    onDatePickerDialogAction: (DatePickerDialogAction) -> Unit,
    onPaymentSelectorAction: (SelectorSheetAction<PaymentMethodType>) -> Unit
) {
    if (state.discardDialog.visible) {
        DiscardDialog(
            onConfirm = { onDialogAction(ConfirmationDialogAction.Confirm) },
            onCancel = { onDialogAction(ConfirmationDialogAction.Dismiss) }
        )
    }

    if (state.currencyDialog.visible) {
        OptionSelectorDialog(
            config = OptionSelectorDialogConfig(
                type = OptionSelectorType.CURRENCY,
                title = stringResource(R.string.dialog_headline_currency_selector),
                selectedCurrency = state.currencyDialog.selected ?: state.currencyDialog.initial,
                onConfirm = { onCurrencySelectorDialogAction(SelectorDialogAction.Apply) },
                onCancel = { onCurrencySelectorDialogAction(SelectorDialogAction.Cancel) },
                onCurrencySelected = { onCurrencySelectorDialogAction(SelectorDialogAction.Select(it)) }
            )
        )
    }

    if (state.datePickerDialog.visible) {
        AppDatePickerDialog(
            config = AppDatePickerDialogConfig(
                visible = state.datePickerDialog.visible,
                selectedDate = state.datePickerDialog.selectedDate,
                onDateSelected = { onDatePickerDialogAction(DatePickerDialogAction.Select(it)) },
                onConfirm = { onDatePickerDialogAction(DatePickerDialogAction.Confirm) },
                onDismiss = { onDatePickerDialogAction(DatePickerDialogAction.Dismiss) }
            )
        )
    }

    if (state.paymentMethodSelector.visible) {
        ModalBottomSheet(
            onDismissRequest = { onPaymentSelectorAction(SelectorSheetAction.Dismiss) }
        ) {
            ListSelectorBottomSheet(
                sheetState = sheetState,
                config = ListSelectorBottomSheetConfig.Payment(
                    selected = state.paymentMethodSelector.selected,
                    onItemSelected = { onPaymentSelectorAction(SelectorSheetAction.Select(it)) }
                ),
                onDismiss = { onPaymentSelectorAction(SelectorSheetAction.Dismiss) }
            )
        }
    }
}