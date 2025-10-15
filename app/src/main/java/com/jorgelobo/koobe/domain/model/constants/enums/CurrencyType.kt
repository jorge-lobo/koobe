package com.jorgelobo.koobe.domain.model.constants.enums

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.UiLabel

enum class CurrencyType : UiLabel {
    EUR, USD, GBP;

    override fun toLabel() = when (this) {
        EUR -> R.string.radio_euro
        USD -> R.string.radio_dollar
        GBP -> R.string.radio_pound
    }
}