package com.jorgelobo.koobe.domain.model.payment

import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.interfaces.HasColor
import com.jorgelobo.koobe.ui.components.model.icons.IconPack

data class PaymentMethod(
    val type: PaymentMethodType,
    val name: String,
    val icon: IconPack,
    override val color: String
) : HasColor