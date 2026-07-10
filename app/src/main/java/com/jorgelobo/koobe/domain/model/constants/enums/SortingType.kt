package com.jorgelobo.koobe.domain.model.constants.enums

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.UiLabel

enum class SortingType : UiLabel {
    NAME_ASC, NAME_DESC, CATEGORY, AMOUNT_ASC, AMOUNT_DESC;

    override fun toLabel() = when (this) {
        NAME_ASC -> R.string.radio_name_asc
        NAME_DESC -> R.string.radio_name_desc
        CATEGORY -> R.string.radio_category
        AMOUNT_ASC -> R.string.radio_amount_asc
        AMOUNT_DESC -> R.string.radio_amount_desc
    }
}