package com.jorgelobo.koobe.utils

import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

fun paymentMethodIcon(type: PaymentMethodType): IconPack = when (type) {
    PaymentMethodType.CASH -> IconPack.CASH
    PaymentMethodType.CARD -> IconPack.CARD
    PaymentMethodType.TRANSFER -> IconPack.TRANSFER
    PaymentMethodType.CRYPTO -> IconPack.CRYPTO
}