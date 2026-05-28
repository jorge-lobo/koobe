package com.jorgelobo.koobe.ui.screen.transactions

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.ui.components.base.numericKeypad.KeypadKey
import com.jorgelobo.koobe.ui.screen.common.bottomSheet.selector.SelectorSheetAction
import com.jorgelobo.koobe.ui.screen.common.dialog.confirmation.ConfirmationDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.datePicker.DatePickerDialogAction
import com.jorgelobo.koobe.ui.screen.common.dialog.selector.SelectorDialogAction
import java.util.Date

/**
 * User intents for the Transaction Editor screen.
 *
 * This sealed interface represents all possible user actions and UI-driven state changes
 * that are sent to the ViewModel for processing.
 */
sealed interface TransactionEditorIntent {

    /** Intents that directly update or modify the editor state. */
    sealed interface State : TransactionEditorIntent {
        data class DescriptionInputChanged(val description: String) : State
        data class AmountKeyPressed(val key: KeypadKey) : State
        data class DateChanged(val date: Date) : State
        data class CurrencyChanged(val currency: CurrencyType) : State
        data class PaymentMethodChanged(val method: PaymentMethodType) : State

        data object AmountResetClicked : State
    }

    /** Intents representing user actions or UI events that trigger side effects or flows. */
    sealed interface Action : TransactionEditorIntent {
        data object SaveClicked : Action
        data object CloseClicked : Action
        data object RequestDeleteTransaction : Action
        data object ChangeCategoryClicked : Action

        data class DiscardDialogUpdated(val action: ConfirmationDialogAction) : Action
        data class DeleteDialogUpdated(val action: ConfirmationDialogAction) : Action
        data class DatePickerDialogUpdated(val action: DatePickerDialogAction) : Action
        data class CurrencySelectorDialogUpdated(val action: SelectorDialogAction<CurrencyType>) : Action
        data class PaymentMethodSelectorUpdated(val action: SelectorSheetAction<PaymentMethodType>) : Action
    }
}