package com.jorgelobo.koobe.ui.screen.transactions

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.transaction.DescriptionSource
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState
import java.util.Date

/**
 * Represents the UI state for the user input fields and associated dialogs/sheets
 * in the transaction creation or editing screen.
 *
 * @property descriptionSource The source or content of the transaction description.
 * @property amountInput The raw string representation of the amount entered by the user.
 * @property date The selected date for the transaction.
 * @property paymentMethodType The currently selected payment method.
 * @property currencyType The currently selected currency.
 * @property discardDialog State for managing the visibility and actions of the discard changes confirmation dialog.
 * @property deleteDialog State for managing the visibility and actions of the elimination confirmation dialog.
 * @property currencyDialog State for managing the currency selection dialog.
 * @property paymentMethodSelector State for managing the payment method selection bottom sheet.
 * @property datePickerDialog State for managing the date picker dialog.
 */
data class UserInputState(
    val descriptionSource: DescriptionSource? = null,
    val amountInput: String? = null,
    val date: Date? = null,
    val paymentMethodType: PaymentMethodType? = null,
    val currencyType: CurrencyType? = null,
    val discardDialog: ConfirmationDialogState? = null,
    val deleteDialog: ConfirmationDialogState? = null,
    val currencyDialog: SelectorDialogState<CurrencyType>? = null,
    val paymentMethodSelector: SelectorSheetState<PaymentMethodType>? = null,
    val datePickerDialog: DatePickerDialogState? = null,
    val isDeleting: Boolean? = null
)