package com.jorgelobo.koobe.domain.model.payment

import androidx.compose.ui.graphics.vector.ImageVector
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.interfaces.HasColor

data class PaymentMethod(
    val type: PaymentMethodType,
    val name: String,
    val icon: ImageVector,
    override val color: String
) : HasColor