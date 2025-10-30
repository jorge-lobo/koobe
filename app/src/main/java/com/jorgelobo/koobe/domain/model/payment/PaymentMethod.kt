package com.jorgelobo.koobe.domain.model.payment

import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType

data class PaymentMethod(
    val type: PaymentMethodType,
    val name: String,
    val icon: ImageVector,
    val color: String
)