package com.jorgelobo.koobe.ui.screen.transactions.state

import com.jorgelobo.koobe.domain.model.constants.enums.AppLanguage
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetState
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogState
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogState
import com.jorgelobo.koobe.utils.date.DateUtils

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