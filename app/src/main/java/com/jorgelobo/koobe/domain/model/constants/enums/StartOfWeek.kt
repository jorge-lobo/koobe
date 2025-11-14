package com.jorgelobo.koobe.domain.model.constants.enums

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.UiLabel

enum class StartOfWeek : UiLabel {
    SUNDAY, MONDAY;

    override fun toLabel() = when (this) {
        SUNDAY -> R.string.radio_sunday
        MONDAY -> R.string.radio_monday
    }
}