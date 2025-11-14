package com.jorgelobo.koobe.domain.model.constants.enums

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.UiLabel

enum class PaymentMethodType : UiLabel {
    CASH, CARD, TRANSFER, CRYPTO;

    override fun toLabel() = when (this) {
        CASH -> R.string.radio_cash
        CARD -> R.string.radio_card
        TRANSFER -> R.string.radio_transfer
        CRYPTO -> R.string.radio_crypto
    }
}