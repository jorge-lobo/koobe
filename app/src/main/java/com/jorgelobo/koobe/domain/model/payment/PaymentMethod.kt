package com.jorgelobo.koobe.domain.model.payment

import com.jorgelobo.koobe.domain.model.constants.PaymentMethodType

data class PaymentMethod(
    val type: PaymentMethodType,
    val icon: Int
)