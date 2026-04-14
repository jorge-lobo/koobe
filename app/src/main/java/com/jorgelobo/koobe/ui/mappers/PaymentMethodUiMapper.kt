package com.jorgelobo.koobe.ui.mappers

import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

fun PaymentMethodType.toIcon(): IconPack = when (this) {
    PaymentMethodType.CASH -> IconPack.CASH
    PaymentMethodType.CARD -> IconPack.CARD
    PaymentMethodType.TRANSFER -> IconPack.TRANSFER
    PaymentMethodType.CRYPTO -> IconPack.CRYPTO
}