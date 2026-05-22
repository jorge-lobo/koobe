package com.jorgelobo.koobe.ui.screen.transactions.state

import com.jorgelobo.koobe.core.model.FieldUpdate
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.transaction.DescriptionSource
import java.util.Date

data class TransactionFormState(
    val description: FieldUpdate<DescriptionSource> = FieldUpdate.Unchanged,
    val amountInput: FieldUpdate<String> = FieldUpdate.Unchanged,
    val date: FieldUpdate<Date> = FieldUpdate.Unchanged,
    val paymentMethod: FieldUpdate<PaymentMethodType> = FieldUpdate.Unchanged,
    val currency: FieldUpdate<CurrencyType> = FieldUpdate.Unchanged,
) {
    val hasChanges: Boolean
        get() = description is FieldUpdate.Updated ||
                amountInput is FieldUpdate.Updated ||
                date is FieldUpdate.Updated ||
                paymentMethod is FieldUpdate.Updated ||
                currency is FieldUpdate.Updated
}