package com.jorgelobo.koobe.ui.screen.transactions.state

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState
import com.jorgelobo.koobe.utils.date.DateUtils

/**
 * Internal UI state for the Transaction screen, including dialogs, loading states, and form flow flags.
 *
 * This state is not directly exposed to the UI layer and is used to coordinate transient UI behavior.
 *
 * @property discardDialog State of the discard confirmation dialog.
 * @property deleteDialog State of the delete confirmation dialog.
 * @property currencyDialog State of the currency selector dialog.
 * @property paymentMethodSelector State of the payment method selector bottom sheet.
 * @property datePickerDialog State of the date picker dialog.
 * @property isSaving Indicates whether a save operation is in progress.
 * @property isDeleting Indicates whether a delete operation is in progress.
 * @property hasTriedToSave Indicates whether the user has attempted to save at least once.
 */
data class TransactionUiStateInternal(
    val discardDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val deleteDialog: ConfirmationDialogState = ConfirmationDialogState(),
    val currencyDialog: SelectorDialogState<CurrencyType> = SelectorDialogState(),
    val paymentMethodSelector: SelectorSheetState<PaymentMethodType> =
        SelectorSheetState(selected = PaymentMethodType.CASH),
    val datePickerDialog: DatePickerDialogState =
        DatePickerDialogState(
            selectedDate = DateUtils.currentDate,
            language = AppLanguage.ENGLISH
        ),
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val hasTriedToSave: Boolean = false
)