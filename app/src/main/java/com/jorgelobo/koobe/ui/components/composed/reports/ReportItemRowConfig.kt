package com.jorgelobo.koobe.ui.components.composed.reports

import androidx.compose.runtime.Stable
import com.jorgelobo.koobe.domain.model.category.Category
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.domain.model.payment.PaymentMethod

@Stable
data class ReportItemRowConfig(
    val category: Category?,
    val paymentMethod: PaymentMethod?,
    val currencyType: CurrencyType,
    val transactionType: TransactionType,
    val percentage: Double,
    val amount: Double,
    val isCategory: Boolean
)