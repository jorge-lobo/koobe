package com.jorgelobo.koobe.domain.model.constants.enums

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.UiLabel

enum class SortingType : UiLabel {
    ALPHABETICAL, CATEGORY, AMOUNT;

    override fun toLabel() = when (this) {
        ALPHABETICAL -> R.string.radio_alphabetical
        CATEGORY -> R.string.radio_category
        AMOUNT -> R.string.radio_amount

    }
}