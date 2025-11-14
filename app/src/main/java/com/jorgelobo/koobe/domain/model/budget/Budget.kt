package com.jorgelobo.koobe.domain.model.budget

import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType

data class Budget(
    val id: Int,
    val categoryId: Int,
    val subcategoryId: Int,
    val period: PeriodType,
    val repeat: Boolean,
    val paymentMethod: PaymentMethodType?,
    val currency: CurrencyType,
    val limitAmount: Double,
    val spentAmount: Double,
    val projectedAmount: Double,
    val dailyAverage: Double
)