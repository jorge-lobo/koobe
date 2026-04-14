package com.jorgelobo.koobe.domain.model.transaction

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class Shortcut(
    val id: Int,
    val name: String,
    val icon: IconPack,
    val categoryId: Int,
    val transactionType: TransactionType,
    val paymentMethod: PaymentMethodType,
    val currency: CurrencyType,
    val amount: Double,
    val repeat: Boolean = false,
    val period: PeriodType? = null
)