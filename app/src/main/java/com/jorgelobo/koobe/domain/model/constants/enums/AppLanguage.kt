package com.jorgelobo.koobe.domain.model.constants.enums

import com.jorgelobo.koobe.R
import com.jorgelobo.koobe.domain.model.constants.UiLabel

enum class AppLanguage : UiLabel {
    ENGLISH, PORTUGUESE;

    override fun toLabel() = when (this) {
        ENGLISH -> R.string.radio_english
        PORTUGUESE -> R.string.radio_portuguese
    }
}