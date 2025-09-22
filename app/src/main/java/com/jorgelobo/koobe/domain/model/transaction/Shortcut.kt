package com.jorgelobo.koobe.domain.model.transaction

import com.jorgelobo.koobe.domain.model.constants.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.PeriodType
import com.jorgelobo.koobe.domain.model.constants.TransactionType

data class Shortcut(
    val id: String,
    val name: String,
    val icon: String,
    val categoryId: Int,
    val transactionType: TransactionType,
    val paymentMethod: PaymentMethodType,
    val currency: CurrencyType,
    val amount: Double,
    val repeat: Boolean = false,
    val period: PeriodType? = null
)