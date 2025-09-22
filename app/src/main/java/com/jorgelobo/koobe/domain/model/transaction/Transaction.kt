package com.jorgelobo.koobe.domain.model.transaction

import com.jorgelobo.koobe.domain.model.constants.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.TransactionType
import java.time.LocalDate

data class Transaction(
    val id: Int,
    val date: LocalDate,
    val description: String?,
    val type: TransactionType,
    val categoryId: Int,
    val subcategoryId: Int?,
    val amount: Double,
    val paymentMethod: PaymentMethodType,
    val currency: CurrencyType
)