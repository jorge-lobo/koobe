package com.jorgelobo.koobe.utils

import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.ui.components.model.icons.IconPayment

fun paymentMethodIcon(paymentMethodType: PaymentMethodType): ImageVector = when (paymentMethodType) {
    PaymentMethodType.CASH -> IconPayment.CASH.icon
    PaymentMethodType.CARD -> IconPayment.CARD.icon
    PaymentMethodType.TRANSFER -> IconPayment.TRANSFER.icon
    PaymentMethodType.CRYPTO -> IconPayment.CRYPTO.icon
}