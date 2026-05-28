package com.jorgelobo.koobe.ui.screen.transactions.state

import com.jorgelobo.koobe.core.model.FieldUpdate
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.transaction.DescriptionSource
import java.util.Date

/**
 * Represents the state of the Transaction form, tracking user input and changes.
 *
 * Each field is wrapped in [FieldUpdate] to distinguish between unchanged and updated values.
 *
 * @property description Transaction description input state.
 * @property amountInput Raw amount input as entered via keypad or text field.
 * @property date Selected transaction date.
 * @property paymentMethod Selected payment method.
 * @property currency Selected currency.
 * @property amountKeypadTouched Indicates whether the amount keypad has been interacted with.
 */
data class TransactionFormState(
    val description: FieldUpdate<DescriptionSource> = FieldUpdate.Unchanged,
    val amountInput: FieldUpdate<String> = FieldUpdate.Unchanged,
    val date: FieldUpdate<Date> = FieldUpdate.Unchanged,
    val paymentMethod: FieldUpdate<PaymentMethodType> = FieldUpdate.Unchanged,
    val currency: FieldUpdate<CurrencyType> = FieldUpdate.Unchanged,
    val amountKeypadTouched: Boolean = false
) {
    /** `true` if any form field has been modified by the user. */
    val hasChanges: Boolean
        get() = description is FieldUpdate.Updated ||
                amountInput is FieldUpdate.Updated ||
                date is FieldUpdate.Updated ||
                paymentMethod is FieldUpdate.Updated ||
                currency is FieldUpdate.Updated
}