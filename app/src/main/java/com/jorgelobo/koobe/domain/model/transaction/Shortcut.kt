package com.jorgelobo.koobe.domain.model.transaction

import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType

data class Shortcut(
    val id: Int,
    val name: String,
    val icon: ImageVector,
    val categoryId: Int,
    val transactionType: TransactionType,
    val paymentMethod: PaymentMethodType,
    val currency: CurrencyType,
    val amount: Double,
    val repeat: Boolean = false,
    val period: PeriodType? = null
)